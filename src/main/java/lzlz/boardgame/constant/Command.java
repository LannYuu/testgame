package lzlz.boardgame.constant;

/**
 * socket发送接收的指令类型
 */
public enum Command {
    /**
     * 失败
     */
    Fail,
    /**
     * 数据
     */
    Data,
    /**
     * 文本消息
     */
    Message,
    /**
     * 加入
     */
    Join,
    /**
     * 准备就绪
     */
    Ready,
    /**
     * 一步
     */
    Move,
    /**
     * 认输
     */
    GiveUp,
    /**
     * 退出
     */
    Leave
}
