package lzlz.boardgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("game")
public class GameController {

    @GetMapping("/hall")
    public String hall(HttpServletResponse resp) {
        return "forward:/static/html/game/hall.html";
    }

}
