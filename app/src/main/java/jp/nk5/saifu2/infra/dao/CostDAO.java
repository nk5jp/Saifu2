package jp.nk5.saifu2.infra.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.ExtraCost;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.NormalCost;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.domain.Template;
import jp.nk5.saifu2.domain.UnControlledCost;
import jp.nk5.saifu2.infra.util.SQLiteBoolean;

public class CostDAO extends DAO<Cost> {

    private CostRepository repository;

    public CostDAO (Context context, CostRepository repository) {
        super(context);
        this.repository = repository;
    }

    public void createCost(Cost cost) throws Exception
    {
        create(cost, "cost");
    }

    public List<Cost> readAll() throws Exception
    {
        return read("select * from cost;", null);
    }

    public List<Cost> readByIdLatestThree(int id) throws Exception
    {
        return read("select * from cost where id = ? order by date desc limit 3;", new String[]{String.valueOf(id)});
    }

    @Override
    Cost transformCursorToEntity(Cursor cursor) throws Exception
    {

        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        int estimate = cursor.getInt(cursor.getColumnIndex("estimate"));
        int result = cursor.getInt(cursor.getColumnIndex("result"));
        MyDate date = new MyDate(cursor.getInt(cursor.getColumnIndex("date")));
        int templateId = cursor.getInt(cursor.getColumnIndex("templateId"));
        boolean isValid = SQLiteBoolean.getBoolean(cursor.getInt(cursor.getColumnIndex("isValid")));

        if (templateId == SpecificId.MeansNull.getId())
        {
            return new ExtraCost(
                    id,
                    estimate,
                    result,
                    name,
                    isValid,
                    date
            );
        }

        Template template = repository.getTemplateById(templateId);

        if (estimate == 0)
        {
            return new UnControlledCost(
                    id,
                    result,
                    isValid,
                    date,
                    template
            );
        }

        return new NormalCost(
                id,
                estimate,
                result,
                isValid,
                date,
                template
        );
    }

    @Override
    ContentValues transformEntityToValues(Cost entity)
    {
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("estimate", entity.getEstimate());
        values.put("result", entity.getResult());
        values.put("date", entity.getDate().getFullDate());
        values.put("isValid", SQLiteBoolean.getInt(entity.isValid()));
        values.put("templateId", entity.getTemplateId());
        return values;
    }

    @Override
    void updateEntityById(Cost entity, long rowId) {
        entity.setId((int)rowId);
    }

}
