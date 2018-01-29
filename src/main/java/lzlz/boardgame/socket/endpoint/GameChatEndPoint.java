package lzlz.boardgame.socket.endpoint;

import lzlz.boardgame.entity.Player;
import lzlz.boardgame.service.RoomService;
import lzlz.boardgame.socket.AbstractChatEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;

/**  
 * 房间聊天服务器  
 * createBy lzlz at 2018/1/26 13:38 
 * @author : lzlz  
 */ 
@ServerEndpoint(value = "/socket/room/chat/{roomId}-{userId}")
@Component
public class GameChatEndPoint extends AbstractChatEndPoint {
    private String roomId;
    private Player user;

    @Autowired
    RoomService roomService;

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("roomId")String roomId,
                       @PathParam("userId")String userId){
        try {
            this.roomId = roomId;
            this.user = roomService.connectChatServer(roomId,userId,session);
            addSession(session,userId);
            session.getBasicRemote().sendText(getSystemPrefix()+"与聊天服务器连接成功");
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException ignored) {
            }
            e.printStackTrace();
        }
    }


    @Override
    public void broadcast(String message, Session thisSession){
        roomService.forEachChatSession(this.roomId,session->{
            String name = thisSession.equals(session)
                    ?"[我]"+user.getName()
                    :user.getName();
            try {
                sendText(session,getUserPrefix(name),getText(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void addSession(Session session, String name){
    }

    @Override
    public void removeSession(Session session) {

    }

    @Override
    public String getUserPrefix(String name){
        return "<span class=\"layui-bg-gray chat-head\">"
                +formater.format(new Date())
                +"&nbsp;"+name+":</span>&nbsp;";
    }




}
