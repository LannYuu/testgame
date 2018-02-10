package lzlz.boardgame.service;

import lzlz.boardgame.constant.PlayerState;
import lzlz.boardgame.core.squaregame.GameSize;
import lzlz.boardgame.core.squaregame.PlayerRole;
import lzlz.boardgame.core.squaregame.entity.Room;
import lzlz.boardgame.core.squaregame.entity.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service("HallService")
public class HallService {
    private static final Map<String,Room> roomMap = new HashMap<>();
    private static final String defaultPlayerName ="菜鸡";

    public User getPlayer(HttpServletRequest request){
        User player = (User)request.getSession().getAttribute("player");
        if(player == null){
            player = new User();
            request.getSession().setAttribute("player",player);
            String playerName = defaultPlayerName+new Random().nextInt(10000);//随机命名
            player.setName(playerName);
            String userId = "PLAYER"+UUID.randomUUID().toString().replaceAll("-", "");
            player.setId(userId);

        }
        return player;
    }
    /**
     * 从 httprequest 中创建房间
     */
    public void createRoom(String roomName,String roomPassword,User player,GameSize size){
        String roomId = "ROOM"+UUID.randomUUID().toString().replaceAll("-", "");
        Room room = new Room();
        room.setId(roomId);
        room.setMessage(roomName);
        room.setPassword(roomPassword);
//        room.setCreator(player.getName());
        room.setCreateTime(new Date());
        room.setSize(size);
        synchronized (roomMap){
            this.addRoom(room);
        }
        if(joinRoom(roomId,player)){
            player.setRoomId(roomId);
        }
    }

    /**
     * 从 httprequest 中加入房间
     * @param roomId 房间UUID
     * @param player 玩家
     * @return userId对应的用户 不存userId对应用户且房间已满返回null
     */
    public boolean joinRoom(String roomId, User player){
        Room room = this.getRoom(roomId);
        if(room ==null)
            return false;
        if (getUserFromRoomById(room,player.getId()) == null) {//如果房间中不存在此用户
            synchronized (this){
                if(room.getBlue()==null){
                    player.setState(PlayerState.Init);
                    player.setPlayerRole(PlayerRole.Blue);
                    player.setRoomId(roomId);
                    room.setBlue(player);
                }else if(room.getRed()==null){
                    player.setState(PlayerState.Init);
                    player.setPlayerRole(PlayerRole.Red);
                    player.setRoomId(roomId);
                    room.setRed(player);
                }
            }
            return true;
        }else {
            return false;
        }
    }

    public List<Room> getRoomList(){
        return roomMap.values().stream()
                .filter(room -> room.getSquareGame()==null)
                .collect(Collectors.toList());
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