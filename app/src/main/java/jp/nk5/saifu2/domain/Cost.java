package jp.nk5.saifu2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Cost {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int result;
    @Getter @Setter
    private boolean isValid;
    @Getter
    private MyDate date;

    public abstract int getEstimate();
    public abstract int getTemplateId();

}
