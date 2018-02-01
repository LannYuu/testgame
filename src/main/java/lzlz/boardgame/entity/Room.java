package lzlz.boardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.RoomState;
import lzlz.boardgame.core.squaregame.SquareGame;

import java.util.List;

@Data
@NoArgsConstructor
public class Room {
    String id;
    String message;
    String password;
    String creator;
    @JsonIgnore
    SquareGame squareGame;
    @JsonIgnore
    RoomState state;
    @JsonIgnore
    List<User> userList;
}
