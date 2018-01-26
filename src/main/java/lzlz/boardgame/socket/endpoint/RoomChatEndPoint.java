package lzlz.boardgame.socket.endpoint;

import lzlz.boardgame.constant.UserRole;
import lzlz.boardgame.entity.User;
import lzlz.boardgame.socket.AbstractChatEndPoint;
import lzlz.boardgame.socket.SessionWrapper;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

/**  
 * 房间聊天服务器  
 * createBy lzlz at 2018/1/26 13:38 
 * @author : lzlz  
 */ 
@ServerEndpoint(value = "/socket/roomchat/{token}")
@Component
public class RoomChatEndPoint extends AbstractChatEndPoint {
    private static CopyOnWriteArraySet<SessionWrapper> sessionSet = new CopyOnWriteArraySet<>();

    @Override @OnOpen
    public void onOpen(Session session,@PathParam("token")String name){
        try {
            super.name=name;
            addSession(session,name);
            session.getBasicRemote().sendText(getSystemPrefix()+"与聊天服务器连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void broadcast(String message, Session thisSession){
        String text = super.getText(message);
        for (SessionWrapper wrapper : sessionSet) {
            try {
                Session session = wrapper.getSession();
                if(session.equals(thisSession)){
                    wrapper.setLastActiveTime(new Date());
                    sendText(session,getUserPrefix("我"),text);
                }else if(session.isOpen()){
                    sendText(session,getUserPrefix(this.name.substring(0,4)),text);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public String getUserPrefix(String name){
        return "<span class=\"layui-bg-gray chat-head\">"
                +formater.format(new Date())
                +"&nbsp;"+name+":</span>&nbsp;";
    }

    @Override
    public void addSession(Session session,String name){
        SessionWrapper wrapper = new SessionWrapper(session,new User(0, UserRole.Normal,""));
        sessionSet.add(wrapper);
    }

    @Override
    public void removeSession(Session session) {
        sessionSet.remove(new SessionWrapper(session,null));//SessionWrapper的equals方法重写为比较session
    }

}
