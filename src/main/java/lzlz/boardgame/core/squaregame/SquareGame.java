package lzlz.boardgame.core.squaregame;

import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.core.Game;
import lzlz.boardgame.core.squaregame.board.Board;
import lzlz.boardgame.core.squaregame.entity.SquareGameData;
import lzlz.boardgame.core.squaregame.entity.User;

import java.util.function.Consumer;

/**
 * “正方形”游戏逻辑
 * createBy lzlz at 2018/1/31 9:32
 * @author : lzlz
 */
@Slf4j
public class SquareGame implements Game{
    private final User blue;//蓝色方 先手
    private final User red;//红色方
    private final Board board;//棋盘
    private final GameSize size;//大小
    private User active;//当前操作的玩家

    private User winner = null;
    private Consumer<PlayerRole> finishCallback;//当游戏结束时返回

    public SquareGame(GameSize size,User blue,User red){
        this.size =size;
        this.board = new Board(size);
        this.blue =blue;
        this.red =red;
        this.blue.setPlayerRole(PlayerRole.Blue);
        this.blue.setScore(0);
        this.red.setPlayerRole(PlayerRole.Red);
        this.red.setScore(0);
        this.active = blue;
    }

    public User getBlue() {
        return blue;
    }

    public User getRed() {
        return red;
    }
    public Board getBoard() {
        return board;
    }
    /**
     * 把前端传来的index转换为board中的坐标
     * @param role blue or red
     * @param index int[] getBoardData()里对应的index
     */
    public MoveResult move(PlayerRole role,int index){
        if (winner!=null){
            log.debug("失败 已结束");
            return MoveResult.Fail;
        }
        if(!active.getRole().equals(role)){
            log.debug(role+"\t 失败 不是你的回合");
            return MoveResult.Fail;
        }
        MoveResult result = getBoard().move(role,index);
        if(result==MoveResult.Pass){
            exchangePlayer();
        }else if(result==MoveResult.Score){
            log.debug(role+"\t得分");
            changeScore(role);
            User winner = CheckWin();
            if (winner != null) {
                this.winner = winner;
                log.debug(winner.getRole()+"\t获得胜利");
                finishCallback.accept(winner.getPlayerRole());
                return MoveResult.Victory;
            }
        }
        return result;
    }

    /**
     * 认输
     */
    public MoveResult giveUp(User user){
        if(this.blue.equals(user)){
            this.winner = red;
            return MoveResult.Victory;
        }
        else if(this.red.equals(user)){
            this.winner = blue;
            return MoveResult.Victory;
        }
        return MoveResult.Fail;
    }

    public int[] getBoardData(){
        return getBoard().getBoardData();
    }

    public SquareGameData getData(){
        SquareGameData data = new SquareGameData();
        data.setBlueScore(this.blue.getScore());
        data.setRedScore(this.red.getScore());
        data.setBoardData(this.getBoardData());
        data.setFinished(this.isFinished());
        return data;
    }

    private void exchangePlayer(){
        if(active.equals(blue))
            active = red;
        else if(active.equals(red))
            active = blue;
    }

    private void changeScore(PlayerRole role){
        if(blue.getPlayerRole().equals(role))
            blue.setScore(blue.getScore()+1);
        else if (red.getPlayerRole().equals(role))
            red.setScore(red.getScore()+1);
    }

    private User CheckWin(){
        int size = this.size.getValue()-1;
        if(blue.getScore()>size*size/2)
            return blue;
        if(red.getScore()>=size*size/2)
            return red;
        return null;
    }
    public boolean isFinished(){
        return this.winner!=null;
    }

    public void setFinishCallback(Consumer<PlayerRole> callback){
        this.finishCallback = callback;
    }



}
