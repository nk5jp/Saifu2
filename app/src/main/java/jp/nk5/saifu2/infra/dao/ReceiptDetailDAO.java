package jp.nk5.saifu2.infra.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import jp.nk5.saifu2.domain.ReceiptDetail;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.repository.ReceiptRepository;

public class ReceiptDetailDAO extends DAO<ReceiptDetail> {

    private ReceiptRepository receiptRepository;
    private CostRepository costRepository;

    public ReceiptDetailDAO(Context context, ReceiptRepository receiptRepository, CostRepository costRepository) {
        super(context);
        this.receiptRepository = receiptRepository;
        this.costRepository = costRepository;
    }

    public void createReceiptDetail(ReceiptDetail receiptDetail) throws Exception
    {
        create(receiptDetail, "receipt_detail");
    }

    public List<ReceiptDetail> readByReceiptId(int receiptId) throws Exception
    {
        return read("select * from receipt_detail whrere receiptId = ?;", new String[]{String.valueOf(receiptId)});
    }

    public void deleteReceiptDetailsByReceiptId(int receiptId) throws Exception
    {
        delete("receiptDetail", "receiptId = ?", new String[]{String.valueOf(receiptId)});
    }

    @Override
    ReceiptDetail transformCursorToEntity(Cursor cursor) throws Exception
    {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        int receiptId = cursor.getInt(cursor.getColumnIndex("receiptId"));
        int costId = cursor.getInt(cursor.getColumnIndex("costId"));
        int value = cursor.getInt(cursor.getColumnIndex("value"));
        return new ReceiptDetail (
            id,
            receiptRepository.getReceiptById(receiptId),
            costRepository.getCostById(costId),
            value
        );
    }

    @Override
    ContentValues transformEntityToValues(ReceiptDetail entity)
    {
        ContentValues values = new ContentValues();
        values.put("receiptId", entity.getReceipt().getId());
        values.put("costId", entity.getCost().getId());
        values.put("value", entity.getValue());
        return values;
    }

    @Override
    void updateEntityById(ReceiptDetail entity, long rowId) {
        entity.setId((int)rowId);
    }

}
