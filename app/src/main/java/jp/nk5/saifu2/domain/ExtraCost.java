package jp.nk5.saifu2.domain;

import lombok.Getter;

public class ExtraCost extends Cost {

    @Getter
    private int plan;

    public ExtraCost(int id, int result, String name, int plan, boolean isValid, MyDate date)
    {
        super(id, name, result, isValid, date);
        this.plan = plan;
    }

}
