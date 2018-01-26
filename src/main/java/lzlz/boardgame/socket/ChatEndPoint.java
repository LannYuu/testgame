package lzlz.boardgame.socket;

import javax.websocket.*;

/**
 * 注意实现类也需要加上这几个注解，不然会出错
 */
public interface ChatEndPoint {
    @OnOpen
    void onOpen(Session session, String name);

    @OnMessage
    void onMessage(String message, Session session);

    @OnClose
    void onClose(Session session);

    @OnError
    void onError(Session session, Throwable error);

    void broadcast(String message, Session thisSession);

    void addSession(Session session,String name);

    void removeSession(Session session);

}
