package lzlz.boardgame.core.squaregame.board;

import lzlz.boardgame.core.squaregame.PlayerRole;

public class BoardEdge {
    private BoardRange topOrLeft;
    private BoardRange rightOrBottom;

    private PlayerRole owner;

    public BoardEdge(){
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

    public boolean setOwner(PlayerRole owner) {
        if(this.owner!=null){
            return false;
        }
        this.owner = owner;
        if (topOrLeft != null) {
            topOrLeft.setOwner(owner);
        }
        if (rightOrBottom != null) {
            rightOrBottom.setOwner(owner);
        }
        return true;
    }
}
