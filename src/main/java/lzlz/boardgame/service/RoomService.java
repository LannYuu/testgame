package lzlz.boardgame.service;

import lzlz.boardgame.entity.Room;
import lzlz.boardgame.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
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
        synchronized (chatSessionMap) {
            room.getUserList().forEach(user -> consumer.accept(chatSessionMap.get(user.getId())));
        }
    }

    /**
     * 离开房间
     */
    public void leaveRoom(String roomId,String userId){
        Room room = hallService.getRoom(roomId);
        if (room ==null) {//如果存在房间，才移除房间中的用户
            return;
        }
        User user = room.getUserList().stream().filter(u->u.getId().equals(userId)).findFirst().orElse(null);
        room.getUserList().remove(user);

        try {
            synchronized (this){
                Session session = chatSessionMap.remove(userId);
                if (session != null) {
                    session.close();
                }
                session = gameSessionMap.remove(userId);
                if (session != null) {
                    session.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
