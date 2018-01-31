package lzlz.boardgame.core.squaregame;

public enum GameSize {
    Three(3),Five(5),Seven(7);

    private int size;
    GameSize(int size){
        this.size =size;
    }
    public int getValue() {
        return size;
    }
}
