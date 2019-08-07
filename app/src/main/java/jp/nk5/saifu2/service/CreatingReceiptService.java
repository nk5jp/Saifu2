package jp.nk5.saifu2.service;

import android.content.Context;

import java.util.List;

import jp.nk5.saifu2.ReceiptActivity;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.Receipt;
import jp.nk5.saifu2.domain.ReceiptDetail;
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

    public List<Cost> getValidCost() throws Exception
    {
        return costRepository.getValidCost();
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

    public Receipt getReceiptById(int id) throws Exception
    {
        return receiptRepository.getReceiptById(id);
    }

    public List<ReceiptDetail> getReceiptDetailById(int id) throws Exception
    {
        return receiptRepository.getReceiptDetailsByReceiptId(id);
    }

    public void deleteReceipt(int id) throws Exception
    {
        accountRepository.depositMoney(receiptRepository.getReceiptById(id).getAccount().getId(), receiptRepository.getReceiptById(id).getSum());
        for (ReceiptDetail detail : receiptRepository.getReceiptDetailsByReceiptId(id))
        {
            costRepository.updateCostById(detail.getCost().getId(), - detail.getValue());
        }
        receiptRepository.deleteReceipt(id);
    }

    public void selectDetail(int id)
    {
        ReceiptDetailViewModel.ReceiptDetailForView detail = updateViewListener.getViewModel().getReceiptDetails().get(id);
        detail.setSelected(!detail.isSelected());
        updateViewListener.updateView();
    }

    /**
     * 選択されている項目に対して消費税を適用する
     * 費目ごとに消費税を計算して税込価格に変更する．
     * ただし消費税は本来総額に発生するので，差額が発生し得る．それは適当な費目に全額追加する．
     * 描画上はこの計算が完了した時点で選択を解除する
     * @param rate 消費税率の整数値，例えば8%の場合は8
     */
    public void calculateTax(int rate)
    {
        List<ReceiptDetailViewModel.ReceiptDetailForView> details = updateViewListener.getViewModel().getValidDetail();
        if (details.size() == 0) return;

        int sum = details.stream().mapToInt(ReceiptDetailViewModel.ReceiptDetailForView::getValue).sum();
        int tax = sum * rate / 100;
        sum = sum + tax;
        updateViewListener.getViewModel().setSum(updateViewListener.getViewModel().getSum() + tax);

        for (ReceiptDetailViewModel.ReceiptDetailForView detail : details)
        {
            int includedPrice = detail.getValue() * (100 + rate) / 100;
            detail.setValue(includedPrice);
            sum -= includedPrice;
            detail.setSelected(false);
        }
        details.get(0).setValue(details.get(0).getValue() + sum);
        updateViewListener.updateView();
    }



}
