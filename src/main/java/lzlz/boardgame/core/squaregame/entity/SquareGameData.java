package lzlz.boardgame.core.squaregame.entity;

import lombok.Data;
import lzlz.boardgame.core.squaregame.PlayerRole;

@Data
public class SquareGameData {
    PlayerRole role;
    int size;
    int redScore;
    int blueScore;
    boolean isFinished;
    int[] boardData;
}
