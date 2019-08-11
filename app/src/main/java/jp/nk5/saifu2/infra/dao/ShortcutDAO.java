package jp.nk5.saifu2.infra.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.Shortcut;

public class ShortcutDAO extends DAO<Shortcut> {

    public ShortcutDAO(Context context) {
        super(context);
    }

    public void createShortcut(Shortcut shortcut) throws Exception
    {
        create (shortcut, "shortcut");
    }

    public Shortcut readShortcutByBoxId(int id) throws Exception
    {
        List<Shortcut> lists = read("select * from shortcut where boxId = ?;", new String[]{String.valueOf(id)});
        if (lists.size() == 0) return null;
        else return lists.get(0);
    }

    public void updateShortcutByBoxId(Shortcut shortcut) throws Exception
    {
        update(shortcut, "shortcut", "boxId = ?", new String[]{String.valueOf(shortcut.getBoxId())});
    }

    public void deleteShortcutByBoxId(int id) throws Exception
    {
        delete ("shortcut", "boxId = ?", new String[]{String.valueOf(id)});
    }

    @Override
    Shortcut transformCursorToEntity(Cursor cursor)
    {

        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        int boxId = cursor.getInt(cursor.getColumnIndex("boxId"));
        int typeId = cursor.getInt(cursor.getColumnIndex("typeId"));
        int firstId = cursor.getInt(cursor.getColumnIndex("firstId"));
        int secondId = cursor.getInt(cursor.getColumnIndex("secondId"));
        int value = cursor.getInt(cursor.getColumnIndex("value"));

        return new Shortcut (
                id,
                name,
                boxId,
                typeId,
                firstId,
                secondId,
                value
        );
    }

    @Override
    ContentValues transformEntityToValues(Shortcut entity)
    {
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("boxId", entity.getBoxId());
        values.put("typeId", entity.getTypeId());
        values.put("firstId", entity.getFirstId());
        values.put("secondId", entity.getSecondId());
        values.put("value", entity.getValue());

        return values;
    }

    @Override
    void updateEntityById(Shortcut entity, long rowId) {
        entity.setId((int)rowId);
    }

}
