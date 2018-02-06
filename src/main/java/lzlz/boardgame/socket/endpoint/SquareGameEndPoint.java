package lzlz.boardgame.socket.endpoint;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.constant.Command;
import lzlz.boardgame.core.squaregame.MoveResult;
import lzlz.boardgame.core.squaregame.SquareGame;
import lzlz.boardgame.entity.CommandData;
import lzlz.boardgame.core.squaregame.entity.Room;
import lzlz.boardgame.core.squaregame.entity.SquareGameData;
import lzlz.boardgame.core.squaregame.entity.User;
import lzlz.boardgame.service.HallService;
import lzlz.boardgame.service.SquareGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/socket/room/game/{roomId}/{userId}")
@Component
@Slf4j
public class SquareGameEndPoint {
    private String roomId;
    private String userId;
    private String userName;
    private static SquareGameService squareGameService;
    private static HallService hallService;

    @Autowired
    void injectRoomService(SquareGameService service){
        squareGameService = service;
    }
    @Autowired
    void injectHallService(HallService service){
        hallService = service;
    }

    @OnOpen
    public void onOpen(Session session,
                               @PathParam("roomId")String roomId,
                               @PathParam("userId")String userId){
        try{
            Room room = hallService.getRoom(roomId);
            if (room == null) {
                log.info("没有此房间");
                session.close();
                return;
            }
            this.roomId = roomId;
            User player = squareGameService.connect2GameServer(hallService.getRoom(roomId),userId,session);
            if (player == null) {
                log.info("房间没有此用户");
                session.close();
                return;
            }
            this.userId = userId;
            this.userName = player.getName();
            this.join();
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException ignored) {
            }
            e.printStackTrace();
        }
    }
    @OnMessage
    public void onMessage(String message){
        CommandData command = JSON.parseObject(message,CommandData.class);
        switch (command.getCommand()) {
            case Message:
                this.sendMessage(command.getTextData());
                break;
            case Move:
                this.move(command.getNumData());
                break;
            case Ready:
                this.ready();
                break;
            case GiveUp:
                this.giveUp();
                break;
            case Leave:
                this.leave();
                break;
        }
    }

    @OnClose
    public void OnClose(Session session){
        log.debug("gamesession-"+session.getId()+"\tclose");
        sendMessage(hallService.getUserFromRoomById(roomId,userId).getName()+"与游戏服务器断开连接");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("gamesession-"+session.getId()+":发生错误");
        error.printStackTrace();
    }
    private void boardcast(String text){
        squareGameService.forEachGameSession(hallService.getRoom(roomId),session -> {
            if(session.isOpen()){
                try {
                    session.getBasicRemote().sendText(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                squareGameService.removeGameSession(userId);
            }
        });
    }

    //发送普通文本消息
    private void sendMessage(String text){
        CommandData msg = new CommandData();
        msg.setCommand(Command.Message);
        msg.setTextData(text);
        boardcast(JSON.toJSONString(msg));
    }
    //一步
    private void move(int index){
        Room room = hallService.getRoom(roomId);
        if (room == null) {
            log.debug("空room:\t"+roomId);
            return;
        }
        SquareGame game = room.getSquareGame();
        if (game == null) {
            log.debug("空game:\t"+roomId);
            return;
        }
        User player = hallService.getUserFromRoomById(room,this.userId);
        if (player == null) {
            log.debug("空player:\t"+userId);
            return;
        }
        MoveResult result = game.move(player.getPlayerRole(),index);
        CommandData cmd = new CommandData();
        if(result.equals(MoveResult.Fail)){
            cmd.setCommand(Command.Fail);
            cmd.setTextData(result.toString());

            boardcast(JSON.toJSONString(cmd));
            return;
        }
        SquareGameData data = room.getSquareGame().getSquareGameData();
        boardcast(JSON.toJSONString(data));
    }

    //认输
    private void join() {
        CommandData commandData = new CommandData();
        commandData.setCommand(Command.Join);
        commandData.setTextData(userName);
        boardcast(JSON.toJSONString(commandData));
    }

    //认输
    private void giveUp() {
        squareGameService.giveup(this.roomId,this.userId);
        CommandData commandData = new CommandData();
        commandData.setCommand(Command.GiveUp);
        commandData.setTextData(hallService.getUserFromRoomById(roomId,userId).getPlayerRole().toString());
        boardcast(JSON.toJSONString(commandData));
    }

    private void ready(){
        Room room = hallService.getRoom(this.roomId);
        boolean isStarted = squareGameService.ready(room,this.userId);
        if(isStarted){
            SquareGameData data = room.getSquareGame().getSquareGameData();
            boardcast(JSON.toJSONString(data));
        }

    }
    private void leave(){
        squareGameService.leaveRoom(this.roomId,this.userId);
        CommandData commandData = new CommandData();
        commandData.setCommand(Command.Leave);
        commandData.setTextData(hallService.getUserFromRoomById(roomId,userId).getPlayerRole().toString());
        boardcast(JSON.toJSONString(commandData));
    }

}
