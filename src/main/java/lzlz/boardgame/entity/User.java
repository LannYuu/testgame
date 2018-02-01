package lzlz.boardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.UserRole;
import lzlz.boardgame.core.squaregame.PlayerRole;

import javax.websocket.Session;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    String id;
    UserRole role;
    String name;
    PlayerRole playerRole;

    @JsonIgnore
    String chatSessionId;
    @JsonIgnore
    String gameSessionId;
}
