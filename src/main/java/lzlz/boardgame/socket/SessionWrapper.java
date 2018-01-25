package lzlz.boardgame.socket;

import javax.websocket.Session;
import java.util.Date;

public class SessionWrapper {
    private final Session session;
    private final String name;
    private Date lastActiveTime;
    public SessionWrapper(Session session, String name){
        this.session = session;
        this.name = name;
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

    public String getName() {
        return name;
    }

    //session相同就是同一个
    @Override
    public boolean equals(Object obj) {
        return obj instanceof SessionWrapper && ((SessionWrapper) obj).session.equals(this.session);
    }
}
