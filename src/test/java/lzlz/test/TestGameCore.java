package lzlz.test;

import lzlz.boardgame.core.squaregame.GameSize;
import lzlz.boardgame.core.squaregame.PlayerRole;
import lzlz.boardgame.core.squaregame.SquareGame;
import lzlz.boardgame.core.squaregame.board.Board;
import org.junit.Test;

public class TestGameCore {
    @Test
    public void test(){
        SquareGame game = new SquareGame(GameSize.Three);
        game.setFinishCallback(role->{
            System.out.println(role+"获得胜利");
        });
        game.move(PlayerRole.Blue,Board.HORIZONTAL,1,1);
        game.move(PlayerRole.Red,Board.HORIZONTAL,2,1);
        game.move(PlayerRole.Blue,Board.VERTICAL,2,1);
        game.move(PlayerRole.Red,Board.VERTICAL,1,1);
        game.move(PlayerRole.Blue,Board.VERTICAL,1,0);
        game.move(PlayerRole.Red,Board.VERTICAL,0,0);
        game.move(PlayerRole.Blue,Board.HORIZONTAL,0,1);
        game.move(PlayerRole.Red,Board.HORIZONTAL,2,0);
        game.move(PlayerRole.Blue,Board.VERTICAL,2,0);
        game.move(PlayerRole.Red,Board.VERTICAL,0,1);
        game.move(PlayerRole.Blue,Board.HORIZONTAL,1,0);
        game.move(PlayerRole.Red,Board.HORIZONTAL,0,0);
        game.getBoard().print();
    }

}
