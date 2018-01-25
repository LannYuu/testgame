package lzlz.boardgame.socket.endpoint;

import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.socket.SessionWrapper;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/socket/hallchat/{name}")
@Component
@Slf4j
public class HallChatEndPoint {
    private static CopyOnWriteArraySet<SessionWrapper> sessionSet = new CopyOnWriteArraySet<>();
    private static SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
//    private static final int TIMEOUT = 60000;//sessionWrapper过期时间

    @OnOpen
    public void onOpen(Session session,@PathParam("name")String name){
        try {
            addSession(session,name);
            session.getBasicRemote().sendText(getSystemInfoPrefix()+"与聊天服务器连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        checkAndBroadcast(message,session);
    }

    /**
     * 遍历检查session是否isOpen,是则发送消息
     */
    private void checkAndBroadcast(String message, Session thisSession){
        for (SessionWrapper wrapper : sessionSet) {
            try {
                Session session = wrapper.getSession();
                if(session.equals(thisSession)){
                    wrapper.setLastActiveTime(new Date());
                }
                if(session.isOpen()){
                    session.getBasicRemote().sendText(getUserInfoPrefix(wrapper.getName())+message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        //SessionWrapper.equals方法 返回 session之间的equals 所以session相同即可remove
        sessionSet.remove(new SessionWrapper(session,null));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("session:"+session.getId()+"发生错误");
        error.printStackTrace();
    }

    private void addSession(Session session,String name){
        SessionWrapper wrapper = new SessionWrapper(session,name);
        sessionSet.add(wrapper);
    }

    private String getSystemInfoPrefix(){
        return "<span class=\"layui-bg-gray chat-head\">"
                +formater.format(new Date())
                +"&nbsp;[系统信息]:</span>&nbsp;";
    }
    private String getUserInfoPrefix(String name){
        int lenth = name.length();
        name = "用户"+name.substring(lenth-4,lenth);
        return "<span class=\"my-bg-green chat-head\">"
                +formater.format(new Date())
                +"&nbsp;"+name+":</span>&nbsp;";
    }
    private String getSpecialUserInfoPrefix(String role,String name){
        return "<span class=\"layui-bg-black chat-head\">"
                +formater.format(new Date())
                +"&nbsp;["+role+"]"+name+":</span>&nbsp;";
    }

}
