package lzlz.boardgame.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.UserRole;

import javax.websocket.Session;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    String id;
    UserRole role;
    String name;

    @JsonIgnore
    Session chatSession;
    @JsonIgnore
    Session gameSession;
}
