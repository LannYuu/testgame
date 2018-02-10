package lzlz.boardgame.controller;

import lzlz.boardgame.core.squaregame.GameSize;
import lzlz.boardgame.entity.CommonMessage;
import lzlz.boardgame.core.squaregame.entity.User;
import lzlz.boardgame.core.squaregame.entity.Room;
import lzlz.boardgame.service.HallService;
import lzlz.boardgame.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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

    @GetMapping("/hall")
    public ModelAndView hall(HttpServletRequest request) {
        hallService.getPlayer(request);
        return new ModelAndView("game/hall");
    }
    @GetMapping("/room")
    public ModelAndView room(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String token = getToken(request.getSession());
        if(token!=null){
            mv.addObject("token",token).setViewName("game/square");
            return mv;
        }
        mv.setViewName("redirect:error/404");
        return mv;
    }

    @GetMapping("/hall/chattarget")
    public @ResponseBody CommonMessage chatServerPath(HttpServletRequest request) {
        String path = request.getContextPath();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        CommonMessage msg = new CommonMessage();
        msg.setData("ws://"+serverName + ":" + port + path+"/socket/hallchat/"+hallService.getPlayer(request).getName());
        return msg;
    }

    @GetMapping("/hall/rooms")
    public @ResponseBody List<Room> getRooms() {
        return hallService.getRoomList();
    }

    @PostMapping("/setplayername")
    public @ResponseBody CommonMessage setPlayerName(CommonMessage msg,HttpServletRequest request) {
        User player = hallService.getPlayer(request);
        if(player.getRoomId()!=null){
            msg.setErrmessage("已在房间中");
            msg.setData(hallService.getPlayer(request).getName());
            return msg;
        }
        //把playerName 放在了 data 属性中
        String playerName = msg.getData();
        playerName = StringUtils.filterSymbol(StringUtils.filterHTML(StringUtils.filterBlank(playerName)));
        if(!"".equals(playerName))
            player.setName(playerName);
        msg.setData(player.getName());
        return msg;
    }

    @PostMapping("/createroom")
    public @ResponseBody CommonMessage createRoom(
            HttpServletRequest request,
            @RequestParam("room-title") String title,
            @RequestParam(value = "room-size", required = false,defaultValue = "0") int size,
            @RequestParam(value = "room-password", required = false) String password){
        CommonMessage msg = new CommonMessage();
        User player = hallService.getPlayer(request);
        if(player.getRoomId()!=null){
            msg.setErrmessage("已在房间中");
            msg.setData(hallService.getPlayer(request).getName());
            return msg;
        }
        title = StringUtils.maxLength(title,100);
        title = StringUtils.filterBlank(title);
        if(title==null||"".equals(title)){
            msg.setErrmessage("房间名不能为空");
            return msg;
        }
        GameSize gameSize =GameSize.getSize(size);
        if (gameSize == null) {
            msg.setErrmessage("游戏大小不合法");
            return msg;
        }
        hallService.createRoom(title, password,player, gameSize);
        if(player.getRoomId()!=null){
            msg.setData(getToken(request.getSession()));
            msg.setMessage("创建成功");
        }
        return msg;
    }

    @PostMapping("/joinroom")
    public @ResponseBody CommonMessage joinRoom(HttpServletRequest request,
                                                  @RequestParam("room-id") String roomId){
        CommonMessage msg = new CommonMessage();
        HttpSession httpSession = request.getSession();
        if(getToken(httpSession)!=null){
            msg.setErrmessage("已在房间中");
        }
        User player = hallService.getPlayer(request);
        if(!hallService.joinRoom(roomId,player)){
            msg.setErrmessage("加入房间失败");
            return msg;
        }
        msg.setData(getToken(request.getSession()));
        msg.setMessage("加入房间成功");
        return msg;
    }



    //如果在hallService找不到 session中player对应的房间则返回null
    private String getToken(HttpSession httpSession){
        User player = (User)httpSession.getAttribute("player");
        if(player!=null){
            String roomId = player.getRoomId();
            if(roomId !=null){
                if (hallService.getUserFromRoomById(roomId, player.getId()) != null) {
                    return roomId+"/"+player.getId();
                }
            }
        }
        return null;
    }
}
