package lzlz.boardgame.service;

import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.entity.Room;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@Slf4j
public class HallService {
    //大厅
    private static Map<String,Room> hall;

    public HallService(){
        hall = new HashMap<>();
    }

    public void CreateRoom(Session session){
        RoomService room = new RoomService(session);
    }




}
