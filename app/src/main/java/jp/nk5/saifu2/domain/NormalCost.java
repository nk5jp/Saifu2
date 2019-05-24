package jp.nk5.saifu2.domain;

import lombok.Getter;

public class NormalCost extends Cost {

    @Getter
    private int plan;
    @Getter
    private TemplateCost templateCost;

    public NormalCost(int id, int result, boolean isValid, MyDate date, TemplateCost templateCost)
    {
        super(id, templateCost.getName(), result, isValid, date);
        this.plan = templateCost.calclatePlan();
        this.templateCost = templateCost;
    }

}
