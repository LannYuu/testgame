package lzlz.test;

import com.alibaba.fastjson.JSON;
import lzlz.boardgame.constant.Command;
import lzlz.boardgame.core.squaregame.entity.SquareGameData;
import lzlz.boardgame.entity.CommandData;
import org.junit.Test;

public class JSONtest {
    @Test
    public void test(){
        CommandData d = new CommandData();
        SquareGameData data = new SquareGameData();
        data.setBoardData(new int[]{1,2,3,4,5});
        d.setCommand(Command.Move);
        d.setData(data);
        System.out.println(JSON.toJSONString(d));

    }
}
