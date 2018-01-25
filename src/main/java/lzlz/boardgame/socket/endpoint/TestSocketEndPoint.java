package lzlz.boardgame.socket.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/socket/chattest/{token}")
@Component
@Slf4j
public class TestSocketEndPoint {
    private static int onlineCount = 0;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<TestSocketEndPoint> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(@PathParam("token")String token, Session session) {
        addOnlineCount();           //在线数加1
        try {
            if(!"abcd".equals(token)){
                session.getBasicRemote().sendText("无权限连接");
                session.close();
                return;
            }
            this.session = session;
            webSocketSet.add(this);//加入set中
            session.setMaxIdleTimeout(60000);
            log.info("有新连接加入！当前在线人数为" + getOnlineCount());
            sendMessage("欢迎加入");
        } catch (IOException e) {
            log.warn("IO异常");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端"+session.getId()+"的消息"+":"+message);
//        log.info(session.getId());
        //群发消息
        for (TestSocketEndPoint item : webSocketSet) {
            if(item.session.equals(session))
            try {
                if (item.session.isOpen()){
                    item.sendMessage(message);
                }else{
                    webSocketSet.remove(item);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("客户端"+session.getId()+"连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.warn("session:"+session.getId()+"发生错误");
        error.printStackTrace();
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    private static int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        TestSocketEndPoint.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        TestSocketEndPoint.onlineCount--;
    }
}
