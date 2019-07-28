package jp.nk5.saifu2.service;

import android.content.Context;

import java.util.List;

import jp.nk5.saifu2.ReceiptActivity;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.repository.ReceiptRepository;
import jp.nk5.saifu2.infra.repository.AccountRepositorySQLite;
import jp.nk5.saifu2.infra.repository.CostRepositorySQLite;
import jp.nk5.saifu2.infra.repository.ReceiptRepositorySQLite;
import jp.nk5.saifu2.view.viewmodel.ReceiptDetailViewModel;

public class CreatingReceiptService {

    private ReceiptRepository receiptRepository;
    private AccountRepository accountRepository;
    private CostRepository costRepository;
    private ReceiptActivity updateViewListener;

    public CreatingReceiptService(Context context, ReceiptActivity updateViewListener) throws Exception
    {
        this.accountRepository = AccountRepositorySQLite.getInstance(context);
        this.costRepository = CostRepositorySQLite.getInstance(context);
        this.receiptRepository = ReceiptRepositorySQLite.getInstance(context, accountRepository, costRepository);
        this.updateViewListener = updateViewListener;

    }

    /**
     * ビューモデルに明細の追加を依頼し，画面の更新を行う．
     * @param cost 追加する明細に紐づく費目
     * @param value 価格
     */
    public void addDetail(Cost cost, int value)
    {
        updateViewListener.getViewModel().addDetail(cost, value);
        updateViewListener.updateView();
    }

    /**
     * 明細を除去し，viewModelの更新と画面への反映を指示する
     * @param position 削除対象とする明細の行番号
     */
    public void deleteDetail(int position)
    {
        updateViewListener.getViewModel().deleteDetail(position);
        updateViewListener.updateView();
    }

    public List<Cost> getValidCost(int year, int month) throws Exception
    {
        return costRepository.getSpecificCost(year, month);
    }

    public List<Account> getAllValidAccount() throws Exception
    {
        return accountRepository.getAllValidAccount();
    }

    /**
     * レシートおよび明細を作成した上で，口座から合計金額を引き落とす．
     * @param account 使用する講座
     * @return 使用後の口座の保有金額
     */
    public int createReceipt(Account account) throws Exception
    {
        int sum = updateViewListener.getViewModel().getSum();
        receiptRepository.setReceipt(
                updateViewListener.getViewModel().getDate(),
                account,
                sum,
                updateViewListener.getViewModel().getReceiptDetails()
        );
        for (ReceiptDetailViewModel.ReceiptDetailForView detail : updateViewListener.getViewModel().getReceiptDetails())
        {
            costRepository.updateCostById(detail.getCost().getId(), detail.getValue());
        }
        accountRepository.depositMoney(account.getId(), -sum);
        return account.getBalance();
    }
}
