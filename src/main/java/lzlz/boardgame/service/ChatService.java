package lzlz.boardgame.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@Slf4j
public class ChatService {
    //大厅
    static CopyOnWriteArraySet<Room> hall;

    public ChatService(){
        hall = new CopyOnWriteArraySet<>();
        new Thread(()->{
            try {
                for (Room room:hall) {
                    if (room.getSessionCount()==0){
                        hall.remove(room);
                    }
                }
                Thread.sleep(60000);
            }catch (Exception ex){
                log.warn(ex.toString()+" "+ex.getMessage());
            }
        }).start();
    }

    public void CreateRoom(Session session){
        Room room = new Room(session);
        hall.add(room);
    }



}
class Room{
    private CopyOnWriteArraySet<Session> sessionList;
    Room(Session session){
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