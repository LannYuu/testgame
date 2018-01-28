package lzlz.boardgame.controller;

import lzlz.boardgame.entity.CommonMessage;
import lzlz.boardgame.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public @ResponseBody CommonMessage createRoom(HttpServletRequest request,
                                                  @RequestParam("room-title") String title,
                                                  @RequestParam(value = "room-password", required = false) String password){
        CommonMessage msg = new CommonMessage();
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute("room")!=null){
            msg.setErrmessage("已在房间中");
            return msg;
        }
        if(title==null||"".equals(title)){
            msg.setErrmessage("房间名不能为空");
            return msg;
        }
        String roomID = roomService.createRoom(title, password);
        msg.setData(roomID);
        msg.setMessage("创建成功");
        httpSession.setAttribute("room",roomID);
        return msg;
    }
}
