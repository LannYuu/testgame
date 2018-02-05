package lzlz.boardgame.core.squaregame.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.PlayerState;
import lzlz.boardgame.core.squaregame.PlayerRole;

@Data
@NoArgsConstructor
public class User {
    String id;
    String name;
    PlayerRole playerRole;
    PlayerState state;
    int score;
}
