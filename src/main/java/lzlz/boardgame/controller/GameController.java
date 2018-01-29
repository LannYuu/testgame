package lzlz.boardgame.controller;

import lzlz.boardgame.entity.CommonMessage;
import lzlz.boardgame.entity.Player;
import lzlz.boardgame.service.RoomService;
import lzlz.boardgame.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("game")
public class GameController {
    @Autowired
    RoomService roomService;

    @GetMapping("/hall")
    public ModelAndView hall() {
        return new ModelAndView("game/hall");
    }
    @GetMapping("/room")
    public ModelAndView room(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        if(request.getSession().getAttribute("gameToken")!=null){
            mv.setViewName("/game/room");
            return mv;
        }
        mv.setViewName("redirect:/error/404");
        return mv;
    }

    @PostMapping("/hall/chattarget")
    public @ResponseBody CommonMessage chatServerPath(HttpServletRequest request) {
        String name = request.getSession().getId();
        String path = request.getContextPath();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        CommonMessage msg = new CommonMessage();
        msg.setData("ws://"+serverName + ":" + port + path+"/socket/hallchat/"+name);
        return msg;
    }

    @RequestMapping("/test")
    public @ResponseBody CommonMessage msg() {
        CommonMessage msg = new CommonMessage();
        msg.setMessage("success");
        return msg;
    }



    @PostMapping("/createroom")
    public @ResponseBody CommonMessage createRoom(HttpServletRequest request,
                                                  @RequestParam("room-title") String title,
                                                  @RequestParam(value = "room-password", required = false) String password){
        CommonMessage msg = new CommonMessage();
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute("gameToken")!=null){
            msg.setErrmessage("已在房间中");
            return msg;
        }
        title = StringUtils.maxLength(title,100);
        title = StringUtils.filterBlank(title);
        if(title==null||"".equals(title)){
            msg.setErrmessage("房间名不能为空");
            return msg;
        }

        String roomId = roomService.createRoom(title, password);
        Player player = roomService.joinRoom(roomId);
        String gameToken = roomId+"-"+player.getId();
        httpSession.setAttribute("gameToken",gameToken);
        msg.setData(gameToken);
        msg.setMessage("创建成功");
        return msg;
    }
}
