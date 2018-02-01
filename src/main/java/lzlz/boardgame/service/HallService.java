package lzlz.boardgame.service;

import lzlz.boardgame.constant.RoomState;
import lzlz.boardgame.entity.Room;
import lzlz.boardgame.entity.User;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.*;
import java.util.function.Consumer;

@Service("HallService")
public class HallService {
    private static final Map<String,Room> roomMap = new HashMap<>();

    private static final int ROOM_SIZE =2;
    /**
     * 从 httprequest 中创建房间
     * @return 房间的UUID
     */
    public String createRoom(String roomName,String roomPassword,String creatorName){
        String roomId = "ROOM"+UUID.randomUUID().toString().replaceAll("-", "");
        Room room = new Room();
        room.setId(roomId);
        room.setMessage(roomName);
        room.setPassword(roomPassword);
        room.setCreator(creatorName);
        room.setState(RoomState.Initial);
        room.setUserList(new ArrayList<>());
        synchronized (roomMap){
            this.addRoom(room);
        }
        return roomId;
    }

    /**
     * 从 httprequest 中加入房间
     * @param roomId 房间UUID
     * @param playerName 玩家名
     * @param userId 如果是新玩家加入输入 null
     * @return userId对应的用户
     */
    public User joinRoom(String roomId, String playerName, String userId){
        if (userId == null || "".equals(userId))
            userId = "PLAYER"+UUID.randomUUID().toString().replaceAll("-", "");
        Room room = this.getRoom(roomId);
        boolean isNewUser = true;
        int size = room.getUserList().size();
        User player = null;
        for (int i = 0; i < room.getUserList().size(); i++) {
            User player1 = room.getUserList().get(i);
            if(player1.getId().equals(userId)){
                isNewUser = false;//如果ID相同说明是同一个用户
                player = player1;
                break;
            }
        }
        synchronized (this){
            if(isNewUser){
                if (size<ROOM_SIZE){//如果是新用户（id不同）且房间人数未达到上线
                    player = new User();
                    player.setId(userId);
                    player.setName(playerName);
                    room.getUserList().add(player);
                }
            }
        }
        if(room.getUserList().size()>= ROOM_SIZE)
            room.setState(RoomState.Full);
        return player;
    }

    public List<Room> getRoomList(){
        return new ArrayList<>(roomMap.values());
    }



    /**
     * 从一个房间中移除用户，若用户为0则移除房间
     */
    public void removeRoom(String roomId){
        synchronized (roomMap){
            roomMap.remove(roomId);
        }
    }

    private void addRoom(Room room){
        roomMap.put(room.getId(), room);
    }
    private void deleteRoom(String roomId){
        roomMap.remove(roomId);
    }

    public Room getRoom(String roomId){
        return roomMap.get(roomId);
    }


}