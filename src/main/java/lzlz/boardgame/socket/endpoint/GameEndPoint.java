package lzlz.boardgame.socket.endpoint;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/socket/room/game/{roomId}-{userId}")
@Component
@Slf4j
public class GameEndPoint {
    @OnOpen
    public void onOpen(Session session,
                               @PathParam("roomId")String roomId,
                               @PathParam("userId")String userId){

    }
    @OnMessage
    public void onMessage(String message , Session session){

    }

    @OnClose
    public void OnClose(Session session){

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("session:"+session.getId()+"发生错误");
        error.printStackTrace();
    }
}
