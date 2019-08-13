package jp.nk5.saifu2.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.Shortcut;
import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.repository.ReceiptRepository;
import jp.nk5.saifu2.domain.repository.ShortcutRepository;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.infra.repository.AccountRepositorySQLite;
import jp.nk5.saifu2.infra.repository.CostRepositorySQLite;
import jp.nk5.saifu2.infra.repository.ReceiptRepositorySQLite;
import jp.nk5.saifu2.infra.repository.ShortcutRepositorySQLite;
import jp.nk5.saifu2.view.fragment.TopFragment;
import jp.nk5.saifu2.view.viewmodel.ReceiptDetailViewModel;

public class ExecutingShortcutService {

    private AccountRepository accountRepository;
    private CostRepository costRepository;
    private ReceiptRepository receiptRepository;
    private TopFragment updateViewListener;
    private ShortcutRepository shortcutRepository;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     */
    public ExecutingShortcutService(Context context, TopFragment updateViewListener) throws Exception
    {
        this.shortcutRepository = ShortcutRepositorySQLite.getInstance(context);
        this.accountRepository = AccountRepositorySQLite.getInstance(context);
        this.costRepository = CostRepositorySQLite.getInstance(context);
        this.receiptRepository = ReceiptRepositorySQLite.getInstance(context, accountRepository, costRepository);
        this.updateViewListener = updateViewListener;
    }

    public void updateShortcutArray() throws Exception
    {
        Shortcut[] shortcuts = updateViewListener.getViewModel().getShortcuts();
        for (int i = 0; i < 6; i++) shortcuts[i] = shortcutRepository.getShortcutByBoxId(i);
    }

    public boolean isShopCase(int position)
    {
        return updateViewListener.getViewModel().getShortcuts()[position].getTypeId() == SpecificId.Shop.getId();
    }

    public boolean isTransferCase(int position)
    {
        return updateViewListener.getViewModel().getShortcuts()[position].getTypeId() == SpecificId.Transfer.getId();
    }

    public Account getAccount(int position, int row) throws Exception
    {
        int accountId;
        if (row == 0) accountId = updateViewListener.getViewModel().getShortcuts()[position].getFirstId();
        else accountId = updateViewListener.getViewModel().getShortcuts()[position].getSecondId();
        return accountRepository.getAccount(accountId);
    }

    private Cost getCost(int position) throws Exception
    {
        int costId = updateViewListener.getViewModel().getShortcuts()[position].getSecondId();
        return costRepository.getCostById(costId);
    }

    public int createReceipt(int position) throws Exception
    {
        Shortcut shortcut = updateViewListener.getViewModel().getShortcuts()[position];
        Account account = getAccount(position, 0);
        Calendar calendar = Calendar.getInstance();
        MyDate date = new MyDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        int sum = shortcut.getValue();
        Cost cost = getCost(position);
        ReceiptDetailViewModel receiptDetailViewModel = new ReceiptDetailViewModel(SpecificId.NotPersisted.getId(), date, sum, new ArrayList<>(), ReceiptDetailViewModel.MyMode.ADD);
        receiptDetailViewModel.addDetail(cost, sum);

        receiptRepository.setReceipt(
                date,
                account,
                sum,
                receiptDetailViewModel.getReceiptDetails()
        );

        costRepository.updateCostById(cost.getId(), sum);
        accountRepository.depositMoneyWithoutHistory(account.getId(), -sum);
        return account.getBalance();
    }

    public int transferMoney(int position) throws Exception
    {
        Shortcut shortcut = updateViewListener.getViewModel().getShortcuts()[position];
        int debitId = shortcut.getFirstId();
        int creditId = shortcut.getSecondId();
        int value = shortcut.getValue();

        accountRepository.transferMoney(debitId, creditId, value);
        return value;
    }


}
