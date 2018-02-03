package lzlz.boardgame.core.squaregame.board;

import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.core.squaregame.MoveResult;
import lzlz.boardgame.core.squaregame.PlayerRole;

@Slf4j
public class BoardEdge {
    private BoardRange topOrLeft;
    private BoardRange rightOrBottom;

    private PlayerRole owner;

    BoardEdge(){
    }

    public void setTopOrLeft(BoardRange topOrLeft) {
        this.topOrLeft = topOrLeft;
    }

    public void setRightOrBottom(BoardRange rightOrBottom) {
        this.rightOrBottom = rightOrBottom;
    }

    public PlayerRole getOwner() {
        return owner;
    }

    public MoveResult setOwner(PlayerRole owner) {
        if(this.owner!=null){
            log.debug("move失败：edge已经被占有");
            return MoveResult.Fail;
        }
        this.owner = owner;
        boolean score =false;
        if (topOrLeft != null) {
            score = topOrLeft.setOwner(owner);
        }
        if (rightOrBottom != null) {
            score =score||rightOrBottom.setOwner(owner);
        }
        return score?MoveResult.Score:MoveResult.Pass;
    }
}
