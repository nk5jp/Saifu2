package jp.nk5.saifu2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Template {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private boolean isControlled;
    @Getter @Setter
    private boolean isValid;

}
