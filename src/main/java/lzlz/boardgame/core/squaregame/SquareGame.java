package lzlz.boardgame.core.squaregame;

import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.constant.GameState;
import lzlz.boardgame.core.Game;
import lzlz.boardgame.core.squaregame.board.Board;
import lzlz.boardgame.core.squaregame.entity.SquareGameData;
import lzlz.boardgame.core.squaregame.entity.User;


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
    private GameState gameState;//游戏状态
    private SquareGameData data;//游戏数据

    private User winner = null;
//    private Consumer<PlayerRole> finishCallback;//当游戏结束时返回

    public SquareGame(GameSize size,User blue,User red){
        this.size =size;
        this.board = new Board(size);
        this.blue =blue;
        this.red =red;
        this.blue.setPlayerRole(PlayerRole.Blue);
        this.blue.setScore(0);
        this.red.setPlayerRole(PlayerRole.Red);
        this.red.setScore(0);
        this.gameState =GameState.Start;
        this.active = blue;
    }

    public User getBlue() {
        return blue;
    }
    public User getRed() {
        return red;
    }
    public User getWinner(){ return winner;}
    public Board getBoard() {
        return board;
    }
    public GameState getState(){return this.gameState;}

    /**
     * 把前端传来的index转换为board中的坐标
     * @param role blue or red
     * @param index int[] getBoardData()里对应的index
     */
    public MoveResult move(PlayerRole role,int index){
        if (!this.gameState.equals(GameState.Start)){
            log.debug("失败\t游戏不是开始状态");
            return MoveResult.Fail;
        }
        if(!active.getPlayerRole().equals(role)){
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
                this.gameState =GameState.Finish;
                log.debug(winner.getPlayerRole()+"\t获得胜利");
//                finishCallback.accept(winner.getPlayerRole());
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
            this.gameState =GameState.Finish;
            return MoveResult.Victory;
        }
        else if(this.red.equals(user)){
            this.winner = blue;
            return MoveResult.Victory;
        }
        return MoveResult.Fail;
    }

    public SquareGameData getSquareGameData(){
        if(this.data==null){
            this.data = new SquareGameData();

        }
        this.data.setBlueScore(this.blue.getScore());
        this.data.setRedScore(this.red.getScore());
        this.data.setBoardData(this.getBoard().getBoardData());
        this.data.setState(this.getState());
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
        if(blue.getScore()>size*size/2){
            return blue;
        }
        if(red.getScore()>=size*size/2){
            return red;
        }
        return null;
    }

//    public void setFinishCallback(Consumer<PlayerRole> callback){
//        this.finishCallback = callback;
//    }



}
