package lzlz.boardgame.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于返回常规的JSON数据
 * createBy lzlz at 2018/1/27 9:52
 * @author : lzlz
 */
@Data
@NoArgsConstructor
public class CommonMessage {
    int id;
    String message;
    String err;
    String data;
}
