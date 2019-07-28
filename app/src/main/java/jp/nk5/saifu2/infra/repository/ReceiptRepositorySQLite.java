package jp.nk5.saifu2.infra.repository;

import android.content.Context;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Receipt;
import jp.nk5.saifu2.domain.ReceiptDetail;
import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.repository.ReceiptRepository;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.infra.dao.ReceiptDAO;
import jp.nk5.saifu2.infra.dao.ReceiptDetailDAO;
import jp.nk5.saifu2.view.viewmodel.ReceiptDetailViewModel;

public class ReceiptRepositorySQLite implements ReceiptRepository {

    private static ReceiptRepositorySQLite instance;
    private ReceiptDAO receiptDAO;
    private ReceiptDetailDAO receiptDetailDAO;
    private List<Receipt> receipts;

    public static ReceiptRepositorySQLite getInstance(Context context, AccountRepository accountRepository, CostRepository costRepository) throws Exception
    {
        if (instance == null)
        {
            instance = new ReceiptRepositorySQLite(context, accountRepository,  costRepository);
        }
        return instance;
    }

    private ReceiptRepositorySQLite(Context context, AccountRepository accountRepository, CostRepository costRepository) throws Exception
    {
        receiptDAO = new ReceiptDAO(context, accountRepository);
        receiptDetailDAO = new ReceiptDetailDAO(context, this, costRepository);
        initialize();
    }

    private void initialize() throws Exception
    {
        receipts = receiptDAO.getAll();
    }

    @Override
    public void setReceipt(MyDate date, Account account, int sum, List<ReceiptDetailViewModel.ReceiptDetailForView> details) throws Exception {
        Receipt receipt = new Receipt(SpecificId.NotPersisted.getId(), date, account, sum);
        receiptDAO.createReceipt(receipt);
        receipts.add(receipt);

        for (ReceiptDetailViewModel.ReceiptDetailForView detailForView : details)
        {
            ReceiptDetail detail = new ReceiptDetail(
                    SpecificId.NotPersisted.getId(),
                    receipt,
                    detailForView.getCost(),
                    detailForView.getValue()
            );
            receiptDetailDAO.createReceiptDetail(detail);
        }
    }

    @Override
    public Receipt getReceiptById(int id)
    {
        Optional<Receipt> optional = receipts.stream()
                .filter(r -> r.getId() == id)
                .findFirst();

        if (optional.isPresent())
        {
            return optional.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<ReceiptDetail> getReceiptDetailsByReceiptId(int receiptId) throws Exception {
        return receiptDetailDAO.readByReceiptId(receiptId);
    }

    @Override
    public List<Receipt> getReceiptsByDate(MyDate date) {
        return receipts.stream()
                .filter(r -> r.getDate().getYear() == date.getYear() && r.getDate().getMonth() == date.getMonth() && r.getDate().getDay() == date.getDay())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReceipt(int id) throws Exception {
        receiptDetailDAO.deleteReceiptDetailsByReceiptId(id);
        receiptDAO.deleteReceiptById(id);
        receipts.remove(getReceiptById(id));
    }
}
