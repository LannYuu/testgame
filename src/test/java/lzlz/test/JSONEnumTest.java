package lzlz.test;

import com.alibaba.fastjson.JSON;
import lzlz.boardgame.entity.CommandData;
import org.junit.Test;

public class JSONEnumTest {
    @Test
    public void test1(){
        CommandData command = JSON.parseObject("{\"command\":\"message\",\"numdata\":123,\"textdata\":\"aaa\"}",
                CommandData.class);
        System.out.println(JSON.toJSONString(command));
    }
}
