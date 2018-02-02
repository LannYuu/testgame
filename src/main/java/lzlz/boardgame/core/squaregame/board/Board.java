package lzlz.boardgame.core.squaregame.board;


import lzlz.boardgame.core.squaregame.GameSize;
import lzlz.boardgame.core.squaregame.PlayerRole;
import lzlz.boardgame.core.squaregame.SquareGame;

/**
 * 棋盘
 * createBy lzlz at 2018/1/31 9:48
 * @author : lzlz
 */
public class Board {
    public static final boolean HORIZONTAL = true;
    public static final boolean VERTICAL = false;
    private final SquareGame game;
    private final int size;
    //水平边
    private BoardEdge[][] horizontalEdgeArr;
    //竖直边
    private BoardEdge[][] verticalEdgeArr;
    //方块区域
    private BoardRange[][] rangeArr;

    public Board(GameSize size, SquareGame game){
        this.size = size.getValue();
        this.game = game;
        init();
    }
    /**
     * BUG未修复
     * createBy lzlz at 2018/1/31 16:35
     * @author : lzlz
     */
    private void init(){
        horizontalEdgeArr = new BoardEdge[size][size-1];
        verticalEdgeArr = new BoardEdge[size][size-1];
        rangeArr = new BoardRange[size-1][size-1];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size-1; j++) {
                horizontalEdgeArr[i][j] = new BoardEdge();
                verticalEdgeArr[i][j] = new BoardEdge();
            }
        }
        for (int i = 0; i < this.size - 1; i++) {
            for (int j = 0; j < this.size - 1; j++) {
                BoardEdge top = horizontalEdgeArr[i][j];
                BoardEdge left = verticalEdgeArr[j][i];
                BoardEdge bottom = horizontalEdgeArr[i+1][j];
                BoardEdge right = verticalEdgeArr[j+1][i];
                BoardRange thisRange = new BoardRange(game,top,right,bottom,left);
                rangeArr[i][j] = thisRange;
                top.setRightOrBottom(thisRange);
                right.setTopOrLeft(thisRange);
                bottom.setTopOrLeft(thisRange);
                left.setRightOrBottom(thisRange);
            }
        }
    }

    public boolean move(PlayerRole role, boolean hOrV, int x, int y){
        if (hOrV == HORIZONTAL){
            BoardEdge edge = horizontalEdgeArr[x][y];
            return edge.setOwner(role);
        }else{
            BoardEdge edge = verticalEdgeArr[x][y];
            return edge.setOwner(role);
        }
    }

    public int[] getBoardData(){
        int[][] data = getData();
        int length = 2*size-1;
        int[]gameData = new int[length*length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(data[i], 0, gameData, i * length, length);
        }
        return gameData;
    }
    private int[][] getData(){
        int[][] printChars = new int[2*size-1][2*size-1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                BoardEdge edge = horizontalEdgeArr[i][j];
                //填入横向边
                int c = getBoardData(edge.getOwner())*10;
                printChars[2*i][2*j+1]=c;
                //填入纵向边
                edge = verticalEdgeArr[i][j];
                c = getBoardData(edge.getOwner())*100;
                printChars[2*j+1][2*i]=c;
            }
        }
        //填入range
        for (int i = 0; i < size -1; i++) {
            for (int j = 0; j < size - 1; j++) {
                BoardRange range = rangeArr[i][j];
                int c = getBoardData(range.getOwner());
                printChars[2*i+1][2*j+1]=c;
            }
        }
        return printChars;
    }
    /**
     * 测试用，打印整个棋盘
     * createBy lzlz at 2018/1/31 12:26
     * @author : lzlz
     */
    public void print(){
        int[][] printData = getData();
        for (int i = 0; i < 2 * size - 1; i++) {

            for (int j = 0; j < 2 * size - 1; j++) {
                System.out.print(printData[i][j]+"\t");
            }
            System.out.println();
        }

    }
    //测试用，打印棋盘
    private int getBoardData(PlayerRole role){
        int[] tags = new int[]{1,2,3};//0是不存在的位置 1是空 2是blue 3是red
        if (role == null)
            return tags[0];
        switch (role) {
            case Blue:
                return tags[1];
            case Red:
                return tags[2];
        }
        return tags[0];
    }

}
