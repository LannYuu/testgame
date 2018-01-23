package lzlz.boardgame.service;

import javax.websocket.Session;
import java.util.concurrent.CopyOnWriteArraySet;

public class RoomService {
    private CopyOnWriteArraySet<Session> sessionList;
    public RoomService(Session session){
        sessionList = new CopyOnWriteArraySet<>();
        sessionList.add(session);
    }
    public void addSession(Session session){
        sessionList.add(session);
    }
    public int getSessionCount(){
        for (Session session:
                sessionList) {
            if(!session.isOpen()){
                sessionList.remove(session);
            }
        }
        return sessionList.size();
    }

}
