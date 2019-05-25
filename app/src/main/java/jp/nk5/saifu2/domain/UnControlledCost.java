package jp.nk5.saifu2.domain;

import jp.nk5.saifu2.domain.util.MyDate;

public class UnControlledCost extends Cost {

    private Template template;

    public UnControlledCost(int id, int result, boolean isValid, MyDate date, Template template)
    {
        super(id, template.getName(), result, isValid, date);
        this.template = template;
    }

    public int getEstimate()
    {
        return 0;
    }

    public int getTemplateId()
    {
        return template.getId();
    }

}
