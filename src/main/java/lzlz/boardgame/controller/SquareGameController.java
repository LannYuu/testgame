package lzlz.boardgame.controller;

import lzlz.boardgame.constant.PlayerState;
import lzlz.boardgame.core.squaregame.SquareGame;
import lzlz.boardgame.core.squaregame.entity.Room;
import lzlz.boardgame.entity.CommonMessage;
import lzlz.boardgame.service.HallService;
import lzlz.boardgame.service.SquareGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("game/1")
public class SquareGameController {
    @Autowired
    HallService hallService;

    @Autowired
    SquareGameService gameService;

    @RequestMapping("ready")
    public @ResponseBody
    CommonMessage ready(@RequestParam("room-id") String roomId,
                        @RequestParam("user-id") String userId){
        CommonMessage msg = new CommonMessage();
        Room room = hallService.getRoom(roomId);
        if (room == null) {
            msg.setErrmessage("null room");
            return msg;
        }
        gameService.ready(room,userId);
        return msg;
    }

    @RequestMapping("leave")
    public @ResponseBody
    CommonMessage leave(@RequestParam("room-id") String roomId,
                        @RequestParam("user-id") String userId){
        CommonMessage msg = new CommonMessage();
        gameService.leaveRoom(roomId,userId,msg);
        return msg;
    }

    @RequestMapping("giveup")
    public @ResponseBody
    CommonMessage giveup(@RequestParam("room-id") String roomId,
                        @RequestParam("user-id") String userId){
        CommonMessage msg = new CommonMessage();
        gameService.giveup(roomId,userId,msg);
        return msg;
    }

}
