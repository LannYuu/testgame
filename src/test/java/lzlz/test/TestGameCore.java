package lzlz.test;

import lzlz.boardgame.core.squaregame.GameSize;
import lzlz.boardgame.core.squaregame.PlayerRole;
import lzlz.boardgame.core.squaregame.SquareGame;
import lzlz.boardgame.core.squaregame.board.Board;
import lzlz.boardgame.core.squaregame.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class TestGameCore {
    private SquareGame game;
    @Before
    public void before(){
        game = new SquareGame(GameSize.Three,new User(),new User());
    }

    @Test
    public void test(){

        game.setFinishCallback(role->{
            System.out.println(role+"获得胜利");
        });
        game.move(PlayerRole.Blue,1);
        game.move(PlayerRole.Red,5);
        game.move(PlayerRole.Blue,7);
        game.move(PlayerRole.Red,11);//此处应得分
        game.move(PlayerRole.Blue,3);//此处应失败 不是blue的回合
        game.move(PlayerRole.Red,3);
        game.move(PlayerRole.Blue,3);//此处应失败 已经被占有
        game.move(PlayerRole.Blue,9);
        game.move(PlayerRole.Red,13);//此处胜利
        game.getBoard().print();
        System.out.println(Arrays.toString(game.getBoard().getBoardData()));
    }

}
