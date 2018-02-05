package lzlz.boardgame.socket.endpoint;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.constant.Command;
import lzlz.boardgame.core.squaregame.MoveResult;
import lzlz.boardgame.entity.CommandData;
import lzlz.boardgame.core.squaregame.entity.Room;
import lzlz.boardgame.core.squaregame.entity.SquareGameData;
import lzlz.boardgame.core.squaregame.entity.User;
import lzlz.boardgame.entity.CommonMessage;
import lzlz.boardgame.service.HallService;
import lzlz.boardgame.service.SquareGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/socket/room/game/{roomId}-{userId}")
@Component
@Slf4j
public class SquareGameEndPoint {
    private Room room;
    private User player;
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
                session.getBasicRemote().sendText(getMessage("没有此房间"));
                session.close();
                return;
            }
            this.room = room;
            User player = squareGameService.connect2GameServer(hallService.getRoom(roomId),userId,session);
            if (player == null) {
                session.getBasicRemote().sendText(getMessage("房间没有此用户"));
                session.close();
                return;
            }
            this.player = player;
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
            case Move:
                move(command.getNumData());
                break;
            case GiveUp:
                giveUp();
                break;
        }
    }

    @OnClose
    public void OnClose(Session session){
        log.debug("gamesession-"+session.getId()+"\tclose");
        boardcast(getMessage(player.getName()+"与游戏服务器断开连接"));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("gamesession-"+session.getId()+":发生错误");
        error.printStackTrace();
    }
    private void boardcast(String text){
        squareGameService.forEachGameSession(room,session -> {
            if(session.isOpen()){
                try {
                    session.getBasicRemote().sendText(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                squareGameService.removeGameSession(player.getId());
            }
        });
    }

    //发送普通文本消息
    private String getMessage(String text){
        CommandData msg = new CommandData();
        msg.setCommand(Command.Message);
        msg.setTextData(text);
        return JSON.toJSONString(msg);
    }
    //一步
    private void move(int index){
        MoveResult result = room.getSquareGame().move(player.getPlayerRole(),index);
        afterMove(result);
    }

    //认输
    private void giveUp() {
        MoveResult result = room.getSquareGame().giveUp(player);
        afterMove(result);

    }
    private void afterMove(MoveResult result){
        if(result.equals(MoveResult.Fail)){
            CommonMessage msg = new CommonMessage();
            msg.setErrmessage(result.toString());
            boardcast(JSON.toJSONString(msg));
            return;
        }
        SquareGameData data = room.getSquareGame().getSquareGameData();
        boardcast(JSON.toJSONString(data));
    }

}
