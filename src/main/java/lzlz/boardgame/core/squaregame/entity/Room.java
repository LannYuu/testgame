package lzlz.boardgame.core.squaregame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.RoomState;
import lzlz.boardgame.core.squaregame.GameSize;
import lzlz.boardgame.core.squaregame.SquareGame;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Room {
    String id;
    String message;
    String password;
    String creator;
    String creatorId;
    Date createTime;
    GameSize size;
    @JsonIgnore
    SquareGame squareGame;
//    @JsonIgnore
//    RoomState state;
    @JsonIgnore
    User blue;
    @JsonIgnore
    User red;
}
