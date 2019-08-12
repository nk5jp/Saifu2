package jp.nk5.saifu2.infra.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class DAO <T> {

    private Context context;

    DAO(Context context)
    {
        this.context = context;
    }

    void create(T entity, String tableName) throws Exception
    {
        ContentValues contentValues = transformEntityToValues(entity);

        try (SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase()) {
            long rowId = db.insert(tableName, null, contentValues);
            if (rowId == -1) {
                throw new Exception();
            } else {
                updateEntityById(entity, rowId);
            }
        }
    }

    List<T> read(String selectQuery, String[] args) throws Exception {
        List<T> list = new ArrayList<>();

        try (
                SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, args)
        ) {
            if (cursor.moveToFirst()) {
                do {
                    T entity = transformCursorToEntity(cursor);
                    list.add(entity);
                } while (cursor.moveToNext());
            }
            return list;
        }
    }

    void update(T entity, String tableName, String condition, String[] args) throws Exception {
        ContentValues contentValues = transformEntityToValues(entity);
        try (SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase()) {
            db.beginTransaction();
            long updateRow = db.update(tableName, contentValues, condition, args);
            if (updateRow == -1) {
                throw new Exception();
            } else {
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
    }

    public void delete(String tableName, String condition, String[] args) throws Exception {
        try (SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase()) {
            db.beginTransaction();
            long deleteRaw = db.delete(tableName, condition, args);
            if (deleteRaw == -1) {
                throw new Exception();
            } else {
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
    }

    abstract T transformCursorToEntity(Cursor cursor) throws Exception;
    abstract ContentValues transformEntityToValues(T entity) throws Exception;
    abstract void updateEntityById(T entity, long rowId) throws Exception;

}
