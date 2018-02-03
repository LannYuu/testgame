package lzlz.boardgame.core.squaregame.board;


import lombok.extern.slf4j.Slf4j;
import lzlz.boardgame.core.squaregame.GameSize;
import lzlz.boardgame.core.squaregame.MoveResult;
import lzlz.boardgame.core.squaregame.PlayerRole;

/**
 * 棋盘
 * createBy lzlz at 2018/1/31 9:48
 * @author : lzlz
 */
@Slf4j
public class Board {
    public static final boolean HORIZONTAL = true;
    public static final boolean VERTICAL = false;
    private final int size;
    //水平边
    private BoardEdge[][] horizontalEdgeArr;
    //竖直边
    private BoardEdge[][] verticalEdgeArr;
    //方块区域
    private BoardRange[][] rangeArr;

    public Board(GameSize size){
        this.size = size.getValue();
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
                BoardRange thisRange = new BoardRange(top,right,bottom,left);
                rangeArr[i][j] = thisRange;
                top.setRightOrBottom(thisRange);
                right.setTopOrLeft(thisRange);
                bottom.setTopOrLeft(thisRange);
                left.setRightOrBottom(thisRange);
            }
        }
    }

    private MoveResult move(PlayerRole role, boolean hOrV, int x, int y){
        if (hOrV == HORIZONTAL){
            log.debug(role+"\t横"+x+"-"+y);
            BoardEdge edge = horizontalEdgeArr[x][y];
            return edge.setOwner(role);
        }else{
            log.debug(role+"\t竖"+x+"-"+y);
            BoardEdge edge = verticalEdgeArr[x][y];
            return edge.setOwner(role);
        }
    }

    /**
     * 把前端传来的index转换为board中的坐标
     * @param role blue or red
     * @param index int[] getBoardData()里对应的index
     */
    public MoveResult move(PlayerRole role, int index){
        int length = 2*this.size-1;
        if(index<0||index>=length*length){
            log.debug("move失败：非法的index 数组越界");
            return MoveResult.Fail;
        }
        int row = index/length;
        int col = index%length;
        int tagRow = row%2;
        int tagCol = col%2;
        if((tagRow==0&&tagCol==0)||(tagRow!=0&&tagCol!=0)){//只留下行列不同为基数或者偶数的值其他为非法
            log.debug("move失败：非法的index 非edge位置");
            return MoveResult.Fail;
        }
        if(tagCol!=0){//是横边输入
            int x =row/2;
            int y =(col-1)/2;
            return move(role,HORIZONTAL,x,y);
        }else{//是纵边输入
            int x =col/2;
            int y =(row-1)/2;
            return move(role,VERTICAL,x,y);
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
    public int[][] getData(){//
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
        /*0是不存在的位置 1是空 3是blue 7是red 为了区别横边纵边和方块 还会分别乘以1,10,100，判断值的时候会 %blue 和 red的值
        因此不能取2和5的倍数


         */
        int[] tags = new int[]{1,3,7};
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
