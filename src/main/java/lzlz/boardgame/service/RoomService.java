package lzlz.boardgame.service;

import lzlz.boardgame.entity.Room;
import lzlz.boardgame.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service("RoomService")
public class RoomService {
    private static final Map<String,Session> chatSessionMap = new HashMap<>();
    private static final Map<String,Session> gameSessionMap = new HashMap<>();

    @Autowired
    HallService hallService;

    public User connect2ChatServer(Room room, String userId, Session chatSession){
        //如果room中有对应的玩家则加入session
        User player = room.getUserList().stream()
                .filter(player1 -> player1.getId().equals(userId))
                .findFirst().orElse(null);
        if (player != null){
            chatSessionMap.put(userId,chatSession);
        }
        return player;
    }

    public User connect2GameServer(Room room, String userId, Session gameSession){
        User player = room.getUserList().stream()
                .filter(player1 -> player1.getId().equals(userId))
                .findFirst().orElse(null);
        if (player != null){
            gameSessionMap.put(userId,gameSession);
        }
        return player;
    }

    /**
     * 从一个房间中移除用户，若用户为0则移除房间
     */
    public void removePlayer(Room room, String userId){
        List<User> userList = room.getUserList();
        int size = userList.size();
        for (int i = 0; i <size;i++) {
            User user = userList.get(i);
            if(userId.equals(user.getId())){
                userList.remove(user);
            }
        }
        if(userList.size()==0)
            hallService.removeRoom(room.getId());
    }

    /**
     * 遍历 roomid对应房间聊天服务器的所有session
     * @param roomId 房间UUID
     * @param consumer 对session的操作
     */
    public void forEachChatSession(String roomId, Consumer<Session> consumer){
        Room room = hallService.getRoom(roomId);
        room.getUserList().forEach(user -> consumer.accept(chatSessionMap.get(user.getId())));
    }

    public void removeChatSession(String userId){
        synchronized (chatSessionMap){
            chatSessionMap.remove(userId);
        }
    }

    public void removeGameSession(String userId){
        synchronized (gameSessionMap){
            gameSessionMap.remove(userId);
        }
    }
}
