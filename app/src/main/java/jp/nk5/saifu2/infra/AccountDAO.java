package jp.nk5.saifu2.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.Account;

public class AccountDAO extends DAO <Account> {

    AccountDAO(Context context)
    {
        super(context);
    }

    List<Account> readAll() throws Exception
    {
        return read("select * from account;", null);
    }

    void createAccount(Account account) throws Exception
    {
        create(account, "account");
    }

    void updateAccount(Account account) throws Exception
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

    public ContentValues transformEntityToValues(Account entity) throws Exception
    {
        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("balance", entity.getBalance());
        values.put("isOpened", SQLiteBoolean.getInt(entity.isOpened()));
        return values;
    }

    public void updateEntityById(Account entity, long rowId) throws Exception
    {
        entity.setId((int)rowId);
    }

}
