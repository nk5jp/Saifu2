package jp.nk5.saifu2.domain;

public class UnControlledCost extends Cost {

    private TemplateCost templateCost;

    public UnControlledCost(int id, int result, boolean isValid, MyDate date, TemplateCost templateCost)
    {
        super(id, templateCost.getName(), result, isValid, date);
        this.templateCost = templateCost;
    }

    public int getPlan()
    {
        return 0;
    }

}
