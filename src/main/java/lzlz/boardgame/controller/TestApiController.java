package lzlz.boardgame.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lzlz.boardgame.constant.UserRole;
import lzlz.boardgame.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


//坑 debug模式 重新加载 目前看来修改RequestMapping 或者 改为 GetMapping/PostMapping要重启程序
@RestController
@RequestMapping("api/test")
public class TestApiController {
    @RequestMapping("/1")
    public @ResponseBody User json() {
        User user = new User();
        user.setId(1);
        user.setName("abc");
        user.setRole(UserRole.Admin);
        return user;
    }

}
