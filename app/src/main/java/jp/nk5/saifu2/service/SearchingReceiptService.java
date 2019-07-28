package jp.nk5.saifu2.service;

import android.content.Context;

import java.util.Calendar;

import jp.nk5.saifu2.ShopActivity;
import jp.nk5.saifu2.domain.repository.ReceiptRepository;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.infra.repository.AccountRepositorySQLite;
import jp.nk5.saifu2.infra.repository.CostRepositorySQLite;
import jp.nk5.saifu2.infra.repository.ReceiptRepositorySQLite;
import jp.nk5.saifu2.view.fragment.CalendarFragment;

public class SearchingReceiptService {

    private ReceiptRepository repository;
    private CalendarFragment updateViewListener;
    private ShopActivity errorListener;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param errorListener エラー表示の依頼先．実態は依頼元のアクティビティ．
     */
    public SearchingReceiptService(Context context, CalendarFragment updateViewListener, ShopActivity errorListener) throws Exception
    {
        this.repository = ReceiptRepositorySQLite.getInstance(context, AccountRepositorySQLite.getInstance(context), CostRepositorySQLite.getInstance(context));
        this.updateViewListener = updateViewListener;
        this.errorListener = errorListener;
    }


    /**
     * カレンダーの選択結果を反映する
     * @param year 選択した年
     * @param month 選択した月
     * @param day　選択した日
     */
    public void selectCalendar(int year, int month, int day) throws Exception
    {
        updateViewListener.getViewModel().setDate(
                new MyDate(year, month, day)
        );
        updateReceiptList();
        updateViewListener.updateView();
    }

    /**
     * 選択した日付を返却する
     * @return カレンダー上で選択している日付
     */
    public MyDate getSelectedDate()
    {
        return updateViewListener.getViewModel().getDate();
    }

    /**
     * 画面モデルのレシートリストを最新化する．これ自体が表示更新処理は担っていない点に注意．
     * 初期通過時のみ，本日日付で画面モデルの更新も行う．
     */
    public void updateReceiptList() throws Exception
    {
        if (updateViewListener.getViewModel().getDate() == null) {
            Calendar calendar = Calendar.getInstance();
            updateViewListener.getViewModel().setDate(
                    new MyDate(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DATE)
                    )
            );
        }

        updateViewListener.getViewModel().setReceipts(
                repository.getReceiptsByDate(
                        updateViewListener.getViewModel().getDate()
                )
        );
    }

}
