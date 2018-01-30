package lzlz.boardgame.service;

import lzlz.boardgame.constant.RoomState;
import lzlz.boardgame.entity.Room;
import lzlz.boardgame.entity.Player;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.*;
import java.util.function.Consumer;

@Service
public class RoomService {
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
    public Player joinRoom(String roomId, String playerName,String userId){
        if (userId == null || "".equals(userId))
            userId = "PLAYER"+UUID.randomUUID().toString().replaceAll("-", "");
        Room room = this.getRoom(roomId);
        boolean isNewUser = true;
        int size = room.getUserList().size();
        Player player = null;
        for (int i = 0; i < room.getUserList().size(); i++) {
            Player player1 = room.getUserList().get(i);
            if(player1.getId().equals(userId)){
                isNewUser = false;//如果ID相同说明是同一个用户
                player = player1;
                break;
            }
        }
        synchronized (this){
            if(isNewUser){
                if (size<ROOM_SIZE){//如果是新用户（id不同）且房间人数未达到上线
                    player = new Player();
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
     * 遍历 roomid对应房间聊天服务器的所有session
     * @param roomId 房间UUID
     * @param consumer 对session的操作
     */
    public void forEachChatSession(String roomId, Consumer<Session> consumer){
        Room room = roomMap.get(roomId);
        room.getUserList().forEach(user ->{
            consumer.accept(user.getChatSession());
        });
    }

    public Player connectChatServer(String roomId, String userId,Session chatSession){
        Room room = this.getRoom(roomId);
        //如果room中有对应的玩家则加入session
        Player player = room.getUserList().stream()
                .filter(player1 -> player1.getId().equals(userId))
                .findFirst().orElse(null);
        if (player != null){
            player.setChatSession(chatSession);
        }
        return player;
    }

    public Player connectGameServer(String roomId, String userId,Session gameSession){
        Room room = this.getRoom(roomId);
        Player player = room.getUserList().stream()
                .filter(player1 -> player1.getId().equals(userId))
                .findFirst().orElse(null);
        if (player != null){
            player.setGameSession(gameSession);
        }
        return player;
    }

    /**
     * 从一个房间中移除用户，若用户为0则移除房间
     */
    public void removePlayerFromARoom(String userId, String roomId){
        List<Player> userList = roomMap.get(roomId).getUserList();
        int size = userList.size();
        for (int i = 0; i <size;i++) {
            Player user = userList.get(i);
            if(userId.equals(user.getId())){
                synchronized (roomMap){
                    if(size==1){
                        roomMap.remove(roomId);
                    }
                    userList.remove(user);
                    break;
                }
            }
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