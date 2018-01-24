package lzlz.boardgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("game")
public class GameHallController {
    @GetMapping("/hall")
    public String hall() {

        return "forward:/static/html/game/hall.html";
    }
}
