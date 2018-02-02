package lzlz.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class JSONtest {
    @Test
    public void test(){
        System.out.println(JSON.toJSONString(new int[10][10]));
    }
}
