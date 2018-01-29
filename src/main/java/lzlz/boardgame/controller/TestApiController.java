package lzlz.boardgame.controller;

import lzlz.boardgame.constant.UserRole;
import lzlz.boardgame.entity.Player;
import org.springframework.web.bind.annotation.*;


//坑 debug模式 重新加载 目前看来修改RequestMapping 或者 改为 GetMapping/PostMapping要重启程序
@RestController
@RequestMapping("api/test")
public class TestApiController {
    @RequestMapping("/1")
    public @ResponseBody
    Player json() {
        Player user = new Player();
        user.setId("aaa");
        user.setName("name");
        user.setRole(UserRole.Admin);
        return user;
    }

}
