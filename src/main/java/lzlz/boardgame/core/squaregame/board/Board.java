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
    void init(){
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

    /**
     * 测试用，打印整个棋盘
     * createBy lzlz at 2018/1/31 12:26
     * @author : lzlz
     */
    public void print(){

        char[][] printChars = new char[2*size-1][2*size-1];
        //初始设置所有字符为\t
//        for (int i = 0; i < 2 * size - 1; i++) {
//            for (int j = 0; j < 2 * size - 1; j++) {
//                printChars[i][j]='\t';
//            }
//        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                BoardEdge edge = horizontalEdgeArr[i][j];
                //填入横向边
                char c = getChar(edge.getOwner());
                printChars[2*i][2*j+1]=c;
                //填入纵向边
                edge = verticalEdgeArr[i][j];
                c = getChar(edge.getOwner());
                printChars[2*j+1][2*i]=c;
            }
        }
        //填入range
        for (int i = 0; i < size -1; i++) {
            for (int j = 0; j < size - 1; j++) {
                BoardRange range = rangeArr[i][j];
                char c = getChar(range.getOwner());
                printChars[2*i+1][2*j+1]=c;
            }
        }
        for (int i = 0; i < 2 * size - 1; i++) {

            for (int j = 0; j < 2 * size - 1; j++) {
                System.out.print(printChars[i][j]+"\t");
            }
            System.out.println();
        }

    }
    //测试用，打印棋盘
    private char getChar(PlayerRole role){
        char[] chars = new char[]{'○','●','×'};
        if (role == null)
            return chars[2];
        switch (role) {
            case Blue:
                return chars[0];
            case Red:
                return chars[1];
        }
        return chars[2];
    }

}
