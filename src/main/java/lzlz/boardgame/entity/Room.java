package lzlz.boardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.RoomState;

import java.util.List;

@Data
@NoArgsConstructor
public class Room {
    String id;
    String message;
    String password;
    String creator;
    @JsonIgnore
    RoomState state;
    @JsonIgnore
    List<Player> userList;
}
