package lzlz.boardgame.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.RoomState;
import lzlz.boardgame.socket.WsSessionWrapper;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    String id;
    String roomMessage;
    RoomState state;
    List<WsSessionWrapper> sessionList;
}
