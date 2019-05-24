package jp.nk5.saifu2.domain;

import lombok.Getter;

public class NormalCost extends Cost {

    @Getter
    private int estimate;
    @Getter
    private Template template;

    public NormalCost(int id, int estimate, int result, boolean isValid, MyDate date, Template template)
    {
        super(id, template.getName(), result, isValid, date);
        this.estimate = estimate;
        this.template = template;
    }

    public int getTemplateId()
    {
        return template.getId();
    }

}
