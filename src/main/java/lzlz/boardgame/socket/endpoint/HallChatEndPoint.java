package lzlz.boardgame.socket.endpoint;

import lzlz.boardgame.constant.UserRole;
import lzlz.boardgame.entity.Player;
import lzlz.boardgame.socket.AbstractChatEndPoint;
import lzlz.boardgame.socket.WsSessionWrapper;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/socket/hallchat/{name}")
@Component
public class HallChatEndPoint extends AbstractChatEndPoint {
    private static CopyOnWriteArraySet<WsSessionWrapper> sessionSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session,@PathParam("name") String name) {
        try {
            super.name=name;
            addSession(session,name);
            session.getBasicRemote().sendText(getSystemPrefix()+"与服务器连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void broadcast(String message, Session thisSession){
        String text = getText(message);
        for (WsSessionWrapper wrapper : sessionSet) {
            try {
                Session session = wrapper.getSession();
                if(session.equals(thisSession)){
                    wrapper.setLastActiveTime(new Date());
                    sendText(session,getSpecialUserInfoPrefix(this.name,"我"),getText(text));
                }else if(session.isOpen()){
                    sendText(session,getUserPrefix(this.name),text);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addSession(Session session, String name) {

        WsSessionWrapper wrapper = new WsSessionWrapper(session,
                new Player("", UserRole.Normal,this.name,null,null));
        sessionSet.add(wrapper);
    }

    @Override
    public void removeSession(Session session) {
        sessionSet.remove(new WsSessionWrapper(session,null));//SessionWrapper的equals方法重写为比较session
    }

}
