package lzlz.boardgame.constant;

public enum RoomState {
    /**
     * 初始化的房间
     */
    Initial,
    /**
     * 已经有用户连接的房间
     */
    Connected,
    /**
     * 准备就绪的房间
     */
    Ready,
    Run;
}
