package lzlz.boardgame.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    String sessionId;
    String roomMessage;
    List<UserSession> userList;
}
