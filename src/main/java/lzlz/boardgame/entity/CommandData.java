package lzlz.boardgame.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lzlz.boardgame.constant.Command;

@Data
@NoArgsConstructor
public class CommandData {
    Command command;
    String textData;
    int numData;
    Object data;
}
