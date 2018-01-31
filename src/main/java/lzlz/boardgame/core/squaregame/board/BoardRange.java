package lzlz.boardgame.core.squaregame.board;

import lzlz.boardgame.core.squaregame.PlayerRole;
import lzlz.boardgame.core.squaregame.SquareGame;

import javax.validation.constraints.NotNull;

public class BoardRange {
    private final SquareGame game;
    private final BoardEdge top;
    private final BoardEdge right;
    private final BoardEdge bottom;
    private final BoardEdge left;

    public BoardRange(@NotNull SquareGame game,@NotNull BoardEdge top,@NotNull  BoardEdge right,
                      @NotNull  BoardEdge bottom,@NotNull  BoardEdge left){
        this.game = game;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }
    private PlayerRole owner;

    public PlayerRole getOwner() {
        return owner;
    }

    public void setOwner(PlayerRole owner) {
        if(top.getOwner()!=null && right.getOwner()!=null
                && bottom.getOwner()!=null && left.getOwner()!=null){
            this.owner = owner;
            this.game.changeScore(owner);
        }
    }
}
