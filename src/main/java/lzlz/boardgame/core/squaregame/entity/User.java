package lzlz.boardgame.core.squaregame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.PlayerState;
import lzlz.boardgame.constant.UserRole;
import lzlz.boardgame.core.squaregame.PlayerRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    String id;
    UserRole role;
    String name;
    PlayerRole playerRole;
    PlayerState state;
    int score;
    @JsonIgnore
    String chatSessionId;
    @JsonIgnore
    String gameSessionId;
}
