package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.BankActivity;
import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.infra.repository.AccountRepositorySQLite;
import jp.nk5.saifu2.view.fragment.TransferHistoryFragment;

/**
 * 振替履歴の検索に関する処理を担う．
 */
public class SearchingTransferService {

    private BankActivity errorListener;
    private TransferHistoryFragment updateViewListener;
    private AccountRepository repository;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param errorListener エラー表示の依頼先．実態は依頼元のアクティビティ．
     */
    public SearchingTransferService(Context context, TransferHistoryFragment updateViewListener, BankActivity errorListener) throws Exception
    {
        this.repository = AccountRepositorySQLite.getInstance(context);
        this.updateViewListener = updateViewListener;
        this.errorListener = errorListener;
    }

    /**
     * 画面モデルの振替リストを最新化する．これ自体が表示更新処理は担っていない点に注意．
     * @param year 対象年
     * @param month 対象月
     */
    public void updateTransferList(int year, int month) throws Exception
    {
        updateViewListener.getViewModel().setTransfers(repository.getSpecificTransfer(year, month));
    }

    /**
     * 年月の桁数を確認し，問題が無ければ該当する振替情報を取得し，画面モデルを最新化して画面表示を更新する．
     * 値が年月として不正であっても（例えば2019013），特に検証はせずそのまま処理を続行する．
     * @param date yyyymm形式の指定年月
     */
    public void getSpecificTransfer(int date)
    {
        try {
            if (!isValid(date)) {
                errorListener.showError("this is obviously invalid date");
                return;
            }

            int year = date / 100;
            int month = date - year * 100;
            updateTransferList(year, month);
            updateViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    /**
     * 入力数字の桁数を検証する
     * @param date 検証対象の数字
     * @return 6桁である
     */
    private boolean isValid(int date)
    {
        return String.valueOf(date).length() == 6;
    }

}
