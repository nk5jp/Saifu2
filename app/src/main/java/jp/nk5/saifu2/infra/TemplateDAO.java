package jp.nk5.saifu2.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.Template;

public class TemplateDAO extends DAO<Template> {

    private Context context;

    TemplateDAO(Context context)
    {
        super(context);
    }

    List<Template> readAll() throws Exception
    {
        return read("select * from template;", null);
    }

    void createTemplate(Template template) throws Exception
    {
        create(template, "template");
    }

    void updateTemplate(Template template) throws Exception
    {
        update(template, "template", "id = ?", new String[]{String.valueOf(template.getId())});
    }

    @Override
    Template transformCursorToEntity(Cursor cursor) {
        return new Template(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("name")),
                SQLiteBoolean.getBoolean(cursor.getInt(cursor.getColumnIndex("isControlled"))),
                SQLiteBoolean.getBoolean(cursor.getInt(cursor.getColumnIndex("isValid")))
        );
    }

    @Override
    ContentValues transformEntityToValues(Template entity) {
        ContentValues values = new ContentValues();
        values.put("id", entity.getId());
        values.put("name", entity.getName());
        values.put("isControlled", SQLiteBoolean.getInt(entity.isControlled()));
        values.put("isValid", SQLiteBoolean.getInt(entity.isValid()));
        return values;
    }

    @Override
    void updateEntityById(Template entity, long rowId) {
        entity.setId((int)rowId);
    }
}
