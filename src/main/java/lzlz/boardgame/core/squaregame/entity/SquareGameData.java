package lzlz.boardgame.core.squaregame.entity;

import lombok.Data;
import lzlz.boardgame.constant.GameState;
import lzlz.boardgame.core.squaregame.PlayerRole;

/*前端 gameData
    role:'Blue',
    size:5,
    blueName:'ABCCCCC',
    blueReady:false,
    blueScore:0,
    redName:'哈哈哈哈哈',
    redReady:false,
    redScore:0,
    state:0,
    boardData:[]
     */
@Data
public class SquareGameData {
    PlayerRole role;
    int size;
    String blueName;
    boolean blueReady;
    int blueScore;
    String redName;
    boolean redReady;
    int redScore;
    GameState state;
    int[] boardData;
}
