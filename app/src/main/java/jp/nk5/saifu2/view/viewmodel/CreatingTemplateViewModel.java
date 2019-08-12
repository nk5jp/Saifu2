package jp.nk5.saifu2.view.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class CreatingTemplateViewModel {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private boolean controlled;

}
