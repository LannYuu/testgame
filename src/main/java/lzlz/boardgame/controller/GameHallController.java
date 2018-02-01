package lzlz.boardgame.controller;

import lzlz.boardgame.entity.CommonMessage;
import lzlz.boardgame.entity.User;
import lzlz.boardgame.entity.Room;
import lzlz.boardgame.service.HallService;
import lzlz.boardgame.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

/**
 * 游戏大厅Controller
 * Create by lzlz at 2018/1/29 23:29
 * @author : lzlz
 */
@Controller
@RequestMapping("game")
public class GameHallController {
    @Autowired
    HallService hallService;

    private static final String defaultPlayerName ="菜鸡";
    @GetMapping("/hall")
    public ModelAndView hall(HttpServletRequest request) {
        request.getSession().setAttribute("playerName",getPlayerName(request));
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

    @GetMapping("/hall/chattarget")
    public @ResponseBody CommonMessage chatServerPath(HttpServletRequest request) {
        String path = request.getContextPath();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        CommonMessage msg = new CommonMessage();
        msg.setData("ws://"+serverName + ":" + port + path+"/socket/hallchat/"+getPlayerName(request));
        return msg;
    }

    @GetMapping("/hall/rooms")
    public @ResponseBody List<Room> getRooms() {
        return hallService.getRoomList();
    }

    @PostMapping("/setplayername")
    public @ResponseBody CommonMessage setPlayerName(CommonMessage msg,HttpServletRequest request) {
        if(request.getSession().getAttribute("gameToken")!=null){
            msg.setErrmessage("已在房间中");
            msg.setData(getPlayerName(request));
            return msg;
        }
        //把playerName 放在了 data 属性中
        String playerName = msg.getData();
        playerName = StringUtils.filterSymbol(StringUtils.filterHTML(StringUtils.filterBlank(playerName)));
        if(!"".equals(playerName))
            request.getSession().setAttribute("playerName",playerName);
        msg.setData(getPlayerName(request));
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

        String roomId = hallService.createRoom(title, password,getPlayerName(request));
        User player = hallService.joinRoom(roomId,getPlayerName(request),null);
        String gameToken = roomId+"-"+player.getId();
        httpSession.setAttribute("gameToken",gameToken);
        msg.setData(gameToken);
        msg.setMessage("创建成功");
        return msg;
    }

    @PostMapping("/joinroom")
    public @ResponseBody CommonMessage joinRoom(HttpServletRequest request,
                                                  @RequestParam("room-id") String roomId,
                                                  @RequestParam(value = "room-password", required = false) String password){
        CommonMessage msg = new CommonMessage();
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute("gameToken")!=null){
            msg.setErrmessage("已在房间中");
            return msg;
        }
        User player = hallService.joinRoom(roomId,getPlayerName(request),null);
        if(player==null){
            msg.setErrmessage("加入房间失败");
            return msg;
        }
        String gameToken = roomId+"-"+player.getId();
        httpSession.setAttribute("gameToken",gameToken);
        msg.setData(gameToken);
        msg.setMessage("加入房间成功");
        return msg;
    }



    private String getPlayerName(HttpServletRequest request){
        Object playerName = request.getSession().getAttribute("playerName");
        if(playerName == null || "".equals(playerName)){
            playerName = defaultPlayerName+new Random().nextInt(1000);
        }
        return playerName.toString();
    }

}
