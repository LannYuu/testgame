package lzlz.boardgame.service;

import lzlz.boardgame.constant.RoomState;
import lzlz.boardgame.entity.Room;
import lzlz.boardgame.socket.WsSessionWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RoomService {
    private static final Map<String,Room> roomMap = new HashMap<>();

    /**
     * 从 httprequest 中创建房间
     * @return 房间的UUID
     */
    public String createRoom(){
        String roomId = UUID.randomUUID().toString().replaceAll("-", "");
        Room room = new Room();
        room.setId(roomId);
        room.setState(RoomState.Initial);
        room.setSessionList(new ArrayList<>());
        synchronized (roomMap){
            this.addRoom(room);
        }
        return roomId;
    }

    /**
     * 从 websocket中加入房间
     * @param roomId 房间UUID
     * @param wsWrapper websocketsession的包装类
     */
    public void joinRoom(String roomId, WsSessionWrapper wsWrapper){
        Room room = this.getRoom(roomId);
        synchronized (roomMap){
            room.setState(RoomState.Connected);
            room.getSessionList().add(wsWrapper);
        }
    }


    private void addRoom(Room room){
        roomMap.put(room.getId(), room);
    }
    private void deleteRoom(String roomId){
        roomMap.remove(roomId);
    }

    private Room getRoom(String roomId){
        return roomMap.get(roomId);
    }
}