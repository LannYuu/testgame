package lzlz.boardgame.core.squaregame;

import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.core.squaregame.board.Board;

import java.util.function.Consumer;

/**
 * “正方形”游戏逻辑
 * createBy lzlz at 2018/1/31 9:32
 * @author : lzlz
 */
@Slf4j
public class SquareGame {
    private final Player blue;//蓝色方 先手
    private final Player red;//红色方
    private final Board board;//棋盘
    private final GameSize size;//大小
    private Player active;//当前操作的玩家

    private boolean finished = false;
    private Consumer<PlayerRole> finishCallback;//当游戏结束时返回

    public SquareGame(GameSize size){
        this.size =size;
        this.board = new Board(size,this);
        this.blue = new Player();
        this.red = new Player();
        blue.setRole(PlayerRole.Blue);
        blue.setScore(0);
        red.setRole(PlayerRole.Red);
        red.setScore(0);
        active = blue;
    }

    public Player getBlue() {
        return blue;
    }

    public Player getRed() {
        return red;
    }
    public Board getBoard() {
        return board;
    }

    public void move(PlayerRole player,boolean hOrV, int x, int y){
        if (finished||!active.getRole().equals(player)){
            log.debug(player+"操作失败");
            return;
        }
        //如果操作成功 交换
        if(board.move(player,hOrV,x,y)){
            log.debug(player+"操作成功");
            exchangePlayer();
        }else{
            log.debug(player+"操作失败");
        }
    }
    private void exchangePlayer(){
        if(active.equals(blue))
            active = red;
        else if(active.equals(red))
            active = blue;
    }

    //由BoardRange来控制
    public void changeScore(PlayerRole role){
        if(blue.getRole().equals(role))
            blue.score++;
        else if (red.getRole().equals(role))
            red.score++;
        Player winner = CheckWin();
        if (winner != null) {
            this.finished = true;
            finishCallback.accept(winner.getRole());
        }
    }

    private Player CheckWin(){
        int size = this.size.getValue()-1;
        if(blue.getScore()>size*size/2)
            return blue;
        if(red.getScore()>=size*size/2)
            return red;
        return null;
    }
    public boolean isFinished(){
        return this.finished;
    }

    public void setFinishCallback(Consumer<PlayerRole> callback){
        this.finishCallback = callback;
    }



}
