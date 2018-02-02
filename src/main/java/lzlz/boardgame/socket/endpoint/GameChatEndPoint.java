package lzlz.boardgame.socket.endpoint;

import lzlz.boardgame.entity.Room;
import lzlz.boardgame.entity.User;
import lzlz.boardgame.service.HallService;
import lzlz.boardgame.service.RoomService;
import lzlz.boardgame.socket.AbstractChatEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
@ServerEndpoint(value = "/socket/room/chat/{roomId}/{userId}")
@Controller
public class GameChatEndPoint extends AbstractChatEndPoint {
    private String roomId;
    private User player;
    //两个坑
    //1.不能直接autowired对象，必须用方法来注入（猜测因为ServerEndpoint在service之前注入）
    //2.ServerEndpoint不是单例的，这里注入service是无法在所有连接的对象中使用的，必须static
    private static RoomService roomService;
    private static HallService hallService;

    @Autowired
    void injectRoomService(RoomService service){
        roomService = service;
    }
    @Autowired
    void injectHallService(HallService service){
        hallService = service;
    }

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("roomId")String roomId,
                       @PathParam("userId")String userId){
        try {
            this.roomId = roomId;
            Room room = hallService.getRoom(roomId);
            if (room == null) {
                session.getBasicRemote().sendText("没有此房间");
                session.close();
                return;
            }
            User player = roomService.connect2ChatServer(hallService.getRoom(roomId),userId,session);
            if (player == null) {
                session.getBasicRemote().sendText("房间没有此用户");
                session.close();
                return;
            }
            this.player = player;
            broadcast(player.getName()+" 与聊天服务器连接成功");
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException ignored) {
            }
            e.printStackTrace();
        }
    }

    @Override @OnClose
    public void onClose(Session session) {
        broadcast(player.getName()+" 与聊天服务器断开连接");
        removeSession(session);
    }

    @Override
    public void broadcast(String message, Session thisSession){
        String text = getText(message);
        roomService.forEachChatSession(this.roomId, session->{
            String name = thisSession.equals(session)
                    ?"[我]"+ player.getName()
                    : player.getName();
            try {
                if(session.isOpen())
                    sendText(session,getUserPrefix(name),text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void broadcast(String message){
        String text = getText(message);
        roomService.forEachChatSession(this.roomId, session->{
            try {
                if(session.isOpen())
                    sendText(session,getSystemPrefix(),text);
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
        roomService.removeChatSession(player.getId());
    }

    @Override
    public String getUserPrefix(String name){
        return "<span class=\"layui-bg-gray chat-head\">"
                +formater.format(new Date())
                +"&nbsp;"+name+":</span>&nbsp;";
    }

}


