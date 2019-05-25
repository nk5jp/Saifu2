package jp.nk5.saifu2.infra.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.domain.Transfer;

public class TransferDAO extends DAO<Transfer> {

    private AccountRepository repository;

    public TransferDAO (Context context, AccountRepository repository) {
        super(context);
        this.repository = repository;
    }

    public void createTransfer(Transfer transfer) throws Exception
    {
        create(transfer, "transfer");
    }

    public List<Transfer> readAll() throws Exception
    {
        return read("select * from transfer;", null);
    }

    @Override
    Transfer transformCursorToEntity(Cursor cursor) throws Exception {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        MyDate myDate = new MyDate(
                cursor.getInt(cursor.getColumnIndex("date"))
        );
        int debitId = cursor.getInt(cursor.getColumnIndex("debitId"));
        int creditId = cursor.getInt(cursor.getColumnIndex("creditId"));
        int value = cursor.getInt(cursor.getColumnIndex("value"));
        if (creditId == SpecificId.MeansNull.getId())
        {
            return new Transfer.Builder(id,
                    myDate,
                    repository.getAccount(debitId),
                    value
            ).build();
        } else {
            return new Transfer.Builder(id,
                    myDate,
                    repository.getAccount(debitId),
                    value
            ).credit(
                    repository.getAccount(creditId)
            ).build();
        }
    }

    @Override
    ContentValues transformEntityToValues(Transfer entity)
    {
        ContentValues values = new ContentValues();
        values.put("date", entity.getMyDate().getFullDate());
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
    void updateEntityById(Transfer entity, long rowId) {
        entity.setId((int)rowId);
    }

}
