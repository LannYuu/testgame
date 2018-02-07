package lzlz.boardgame.controller;

import lzlz.boardgame.core.squaregame.entity.SquareGameData;
import lzlz.boardgame.entity.CommonMessage;
import lzlz.boardgame.service.HallService;
import lzlz.boardgame.service.SquareGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 这个controller只需保留离开房间：保证不支持websocket也可以离开房间
 */
@RestController
@RequestMapping("game/1")
public class SquareGameController {
    @Autowired
    HallService hallService;

    @Autowired
    SquareGameService gameService;


    @RequestMapping("fullData/{roomId}/{userId}")
    public @ResponseBody
    SquareGameData fullData(@PathVariable("roomId") String roomId,
                            @PathVariable("userId") String userId){
        return gameService.fullData(hallService.getRoom(roomId),hallService.getUserFromRoomById(roomId, userId));
    }

    @RequestMapping("leave/{roomId}/{userId}")
    public @ResponseBody
    CommonMessage leave(@PathVariable("roomId") String roomId,
                        @PathVariable("userId") String userId){
        CommonMessage msg = new CommonMessage();
        gameService.leaveRoom(roomId,userId);
        msg.setMessage("离开房间");
        return msg;
    }

}
