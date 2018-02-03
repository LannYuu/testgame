package lzlz.boardgame.socket.endpoint;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.entity.CommonMessage;
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

@ServerEndpoint(value = "/socket/room/game/{roomId}-{userId}")
@Component
@Slf4j
public class SquareGameEndPoint {
    private String roomId;
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
            this.roomId = roomId;
            Room room = hallService.getRoom(roomId);
            if (room == null) {
                session.getBasicRemote().sendText(getMessage("没有此房间"));
                session.close();
                return;
            }
            User player = squareGameService.connect2ChatServer(hallService.getRoom(roomId),userId,session);
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
    public void onMessage(String message , Session session){

    }

    @OnClose
    public void OnClose(Session session){

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("gamesession-"+session.getId()+":发生错误");
        error.printStackTrace();
    }

    //由于数据较为复杂 采用json字符串传递
    private String getMessage(String text){
        CommonMessage msg = new CommonMessage();
        msg.setMessage(text);
        return JSON.toJSONString(msg);
    }

    private String getData(SquareGameData data){
        return JSON.toJSONString(data);
    }
}
