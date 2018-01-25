package lzlz.boardgame.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/game")
public class GameHallApiController {
    @PostMapping("/hallchat")
    public String chatServerPath(HttpServletRequest request) {
        String name = request.getSession().getId();
        String path = request.getContextPath();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        return "ws://"+serverName + ":" + port + path+"/socket/hallchat/"+name;
    }
}
