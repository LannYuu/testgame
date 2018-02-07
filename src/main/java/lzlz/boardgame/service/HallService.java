package lzlz.boardgame.service;

import lzlz.boardgame.constant.PlayerState;
import lzlz.boardgame.constant.RoomState;
import lzlz.boardgame.core.squaregame.GameSize;
import lzlz.boardgame.core.squaregame.PlayerRole;
import lzlz.boardgame.core.squaregame.entity.Room;
import lzlz.boardgame.core.squaregame.entity.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("HallService")
public class HallService {
    private static final Map<String,Room> roomMap = new HashMap<>();

    /**
     * 从 httprequest 中创建房间
     * @return 拼接的两个Id:roomId-userId
     */
    public String createRoom(String roomName,String roomPassword,String creatorName,GameSize size){
        String roomId = "ROOM"+UUID.randomUUID().toString().replaceAll("-", "");
        Room room = new Room();
        room.setId(roomId);
        room.setMessage(roomName);
        room.setPassword(roomPassword);
        room.setCreator(creatorName);
        room.setCreateTime(new Date());
        room.setSize(size);
        synchronized (roomMap){
            this.addRoom(room);
        }
        User newUser = joinRoom(roomId,creatorName,null);
        return roomId+"/"+newUser.getId();
    }

    /**
     * 从 httprequest 中加入房间
     * @param roomId 房间UUID
     * @param playerName 玩家名
     * @param userId 如果是新玩家加入输入 null
     * @return userId对应的用户 不存userId对应用户且房间已满返回null
     */
    public User joinRoom(String roomId, String playerName, String userId){
        if (userId == null || "".equals(userId))
            userId = "PLAYER"+UUID.randomUUID().toString().replaceAll("-", "");
        Room room = this.getRoom(roomId);
        if(room ==null)
            return null;
        User player = getUserFromRoomById(room,userId);
        if (player == null) {
            synchronized (this){
                if(room.getBlue()==null){
                    player = new User();
                    player.setId(userId);
                    player.setState(PlayerState.Init);
                    player.setName(playerName);
                    player.setPlayerRole(PlayerRole.Blue);
                    room.setBlue(player);
                }else if(room.getRed()==null){
                    player = new User();
                    player.setId(userId);
                    player.setState(PlayerState.Init);
                    player.setName(playerName);
                    player.setPlayerRole(PlayerRole.Red);
                    room.setRed(player);
                }
            }
        }
        return player;
    }

    public List<Room> getRoomList(){
        return new ArrayList<>(roomMap.values());
    }

    public Room removeRoom(String roomId){
        synchronized (roomMap){
            return roomMap.remove(roomId);
        }
    }

    private void addRoom(Room room){
        roomMap.put(room.getId(), room);
    }

    public Room getRoom(String roomId){
        return roomMap.get(roomId);
    }


    public User getUserFromRoomById(String roomId,String userId){
        Room room = getRoom(roomId);
        if (room != null) {
            return getUserFromRoomById(room,userId);
        }
        return null;
    }
    public User getUserFromRoomById(Room room,String userId){
        User blue = room.getBlue();
        if (blue != null) {
            if(userId.equals(blue.getId())) {
                return blue;
            }
        }
        User red = room.getRed();
        if (red != null) {
            if(userId.equals(red.getId()))
                return red;
        }
        return null;
    }
}