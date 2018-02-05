package lzlz.boardgame.constant;

public enum UserLevel {
    Admin("admin"),
    Normal("normal");

    private String value;
    UserLevel(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
