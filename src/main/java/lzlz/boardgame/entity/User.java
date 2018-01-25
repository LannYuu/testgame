package lzlz.boardgame.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    int id;
    UserRole role;
    String name;
}
