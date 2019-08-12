package jp.nk5.saifu2.infra.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.Receipt;
import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.domain.util.MyDate;

public class ReceiptDAO extends DAO <Receipt> {

    private AccountRepository repository;

    public ReceiptDAO(Context context, AccountRepository repository) {
        super(context);
        this.repository = repository;
    }

    public void createReceipt(Receipt receipt) throws Exception
    {
        create (receipt, "receipt");
    }

    public List<Receipt> getAll() throws Exception
    {
        return read("select * from receipt;", null);
    }

    /**
     * detailの削除はrepository側で調整する
     * @param id 削除対象のid
     */
    public void deleteReceiptById(int id) throws Exception
    {
        delete ("receipt", "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    Receipt transformCursorToEntity(Cursor cursor) throws Exception
    {

        int id = cursor.getInt(cursor.getColumnIndex("id"));
        MyDate date = new MyDate(cursor.getInt(cursor.getColumnIndex("date")));
        int accountId = cursor.getInt(cursor.getColumnIndex("accountId"));
        int sum = cursor.getInt(cursor.getColumnIndex("sum"));

        return new Receipt (
                id,
                date,
                repository.getAccount(accountId),
                sum
        );
    }

    @Override
    ContentValues transformEntityToValues(Receipt entity)
    {
        ContentValues values = new ContentValues();
        values.put("date", entity.getDate().getFullDate());
        values.put("accountId", entity.getAccount().getId());
        values.put("sum", entity.getSum());
        return values;
    }

    @Override
    void updateEntityById(Receipt entity, long rowId) {
        entity.setId((int)rowId);
    }

}
