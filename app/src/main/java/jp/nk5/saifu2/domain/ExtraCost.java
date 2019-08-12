package jp.nk5.saifu2.domain;

import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.util.SpecificId;
import lombok.Getter;

public class ExtraCost extends Cost {

    @Getter
    private int estimate;

    public ExtraCost(int id, int estimate, int result, String name, boolean isValid, MyDate date)
    {
        super(id, name, result, isValid, date);
        this.estimate = estimate;
    }

    public int getTemplateId()
    {
        return SpecificId.MeansNull.getId();
    }

}
