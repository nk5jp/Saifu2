package jp.nk5.saifu2.infra.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.infra.util.SQLiteBoolean;

public class AccountDAO extends DAO<Account> {

    public AccountDAO(Context context)
    {
        super(context);
    }

    public List<Account> readAll() throws Exception
    {
        return read("select * from account;", null);
    }

    public void createAccount(Account account) throws Exception
    {
        create(account, "account");
    }

    public void updateAccount(Account account) throws Exception
    {
        update(account, "account", "id = ?", new String[]{String.valueOf(account.getId())});
    }


    public Account transformCursorToEntity(Cursor cursor) throws Exception
    {
         return new Account(
                 cursor.getInt(cursor.getColumnIndex("id")),
                 cursor.getString(cursor.getColumnIndex("name")),
                 cursor.getInt(cursor.getColumnIndex("balance")),
                 SQLiteBoolean.getBoolean(cursor.getInt(cursor.getColumnIndex("isOpened")))
         );
    }

    public ContentValues transformEntityToValues(Account entity)
    {
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("balance", entity.getBalance());
        values.put("isOpened", SQLiteBoolean.getInt(entity.isOpened()));
        return values;
    }

    public void updateEntityById(Account entity, long rowId)
    {
        entity.setId((int)rowId);
    }

}
