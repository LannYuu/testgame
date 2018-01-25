package lzlz.boardgame.constant;

public enum UserRole {
    Admin("admin"),
    Normal("normal");

    private String value;
    UserRole(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
}
