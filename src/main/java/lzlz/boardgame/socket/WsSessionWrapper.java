package lzlz.boardgame.socket;

import lzlz.boardgame.entity.User;

import javax.websocket.Session;
import java.util.Date;

/**
 * websocket session 包装类
 * createBy lzlz at 2018/1/27 9:29
 * @author : lzlz
 */
public class WsSessionWrapper {
    private final Session session;
    private final User user;
    private Date lastActiveTime;
    public WsSessionWrapper(Session session, User user){
        this.session = session;
        this.user = user;
        lastActiveTime = new Date();
    }

    /**
     * session的闲置时间在接收消息后也会重置，这里的闲置时间为了实现只有发送消息后重置
     * @return 闲置时间(ms)
     */
    public long getIdleTime(){
        return new Date().getTime()- lastActiveTime.getTime();
    }
    public void setLastActiveTime(Date time){
        this.lastActiveTime = time;
    }

    public Session getSession() {
        return session;
    }

    public User getUser() {
        return user;
    }

    //session相同就是同一个
    @Override
    public boolean equals(Object obj) {
        return obj instanceof WsSessionWrapper && ((WsSessionWrapper) obj).session.equals(this.session);
    }
}
