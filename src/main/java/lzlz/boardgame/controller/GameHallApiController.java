package lzlz.boardgame.controller;

import lzlz.boardgame.entity.CommonMessage;
import lzlz.boardgame.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api/game")
public class GameHallApiController {
    @Autowired
    RoomService roomService;

    @PostMapping("/hallchat")
    public String chatServerPath(HttpServletRequest request) {
        String name = request.getSession().getId();
        String path = request.getContextPath();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        return "ws://"+serverName + ":" + port + path+"/socket/hallchat/"+name;
    }

    @RequestMapping("/createroom")
    public @ResponseBody CommonMessage createRoom(HttpServletRequest request){
        CommonMessage msg = new CommonMessage();
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute("room")!=null){
            msg.setId(-1);
            msg.setMessage("已在房间中");
            return msg;
        }
         String roomID = roomService.createRoom();
        msg.setData("roomId");
        msg.setMessage("");
        return msg;
    }
}
