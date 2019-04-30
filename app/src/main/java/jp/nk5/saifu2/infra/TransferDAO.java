package jp.nk5.saifu2.infra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.domain.MyDate;
import jp.nk5.saifu2.domain.SpecificId;
import jp.nk5.saifu2.domain.Transfer;

class TransferDAO extends DAO<Transfer> {

    private AccountRepository repository;

    TransferDAO (Context context, AccountRepository repository) {
        super(context);
        this.repository = repository;
    }

    void createTransfer(Transfer transfer) throws Exception
    {
        create(transfer, "transfer");
    }

    List<Transfer> readAll() throws Exception
    {
        return read("select * from transfer;", null);
    }

    @Override
    Transfer transformCursorToEntity(Cursor cursor) throws Exception {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        int date = cursor.getInt(cursor.getColumnIndex("date"));
        int debitId = cursor.getInt(cursor.getColumnIndex("debitId"));
        int creditId = cursor.getInt(cursor.getColumnIndex("creditId"));
        int value = cursor.getInt(cursor.getColumnIndex("value"));
        if (creditId == SpecificId.MeansNull.getId())
        {
            return new Transfer.Builder(id,
                    MyDate.getYear(date),
                    MyDate.getMonth(date),
                    MyDate.getDay(date),
                    repository.getAccount(debitId),
                    value
            ).build();
        } else {
            return new Transfer.Builder(id,
                    MyDate.getYear(date),
                    MyDate.getMonth(date),
                    MyDate.getDay(date),
                    repository.getAccount(debitId),
                    value
            ).credit(
                    repository.getAccount(creditId)
            ).build();
        }
    }

    @Override
    ContentValues transformEntityToValues(Transfer entity) throws Exception {
        ContentValues values = new ContentValues();
        values.put("date", MyDate.generateDate(entity.getYear(), entity.getMonth(), entity.getDay()));
        values.put("debitId", entity.getDebit().getId());
        values.put("value", entity.getValue());
        switch(entity.getTransferType())
        {
            case Deposit:
                values.put("creditId", SpecificId.MeansNull.getId());
                break;
            case Transfer:
                values.put("creditId", entity.getCredit().getId());
                break;
        }
        return values;
    }

    @Override
    void updateEntityById(Transfer entity, long rowId) throws Exception {
        entity.setId((int)rowId);
    }

}
