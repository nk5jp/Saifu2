package jp.nk5.saifu2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Shortcut {

    @Setter @Getter
    private int id;
    @Setter @Getter
    private String name;
    @Setter @Getter
    private int boxId;
    @Setter @Getter
    private int typeId;
    @Setter @Getter
    private int firstId;
    @Setter @Getter
    private int secondId;
    @Setter @Getter
    private int value;

}
