package jp.nk5.saifu2.domain.repository;

import java.util.List;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Receipt;
import jp.nk5.saifu2.domain.ReceiptDetail;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.view.viewmodel.ReceiptDetailViewModel;

public interface ReceiptRepository {
    void setReceipt(MyDate date, Account account, int sum, List<ReceiptDetailViewModel.ReceiptDetailForView> details) throws Exception;
    Receipt getReceiptById(int id) throws Exception;
    List<ReceiptDetail> getReceiptDetailsByReceiptId(int receiptId) throws Exception;
    List<Receipt> getReceiptsByDate(MyDate date) throws Exception;
    void deleteReceipt(int id) throws Exception;
}
