package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.AccountBookActivity;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.infra.repository.CostRepositorySQLite;
import jp.nk5.saifu2.view.fragment.CostFragment;

public class SearchingCostService {

    private AccountBookActivity errorListener;
    private CostFragment updateViewListener;
    private CostRepository repository;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param errorListener エラー表示の依頼先．実態は依頼元のアクティビティ．
     */
    public SearchingCostService(Context context, CostFragment updateViewListener, AccountBookActivity errorListener) throws Exception
    {
        this.repository = CostRepositorySQLite.getInstance(context);
        this.updateViewListener = updateViewListener;
        this.errorListener = errorListener;
    }

    /**
     * 画面モデルの費目リストを最新化する．これ自体が表示更新処理は担っていない点に注意．
     * @param year 対象年
     * @param month 対象月
     */
    public void updateCostList(int year, int month) throws Exception
    {
        updateViewListener.getViewModel().setCosts(repository.getSpecificCost(year, month));
    }

    public void getSpecificCost(int date)
    {
        try {
            if (!isValid(date)) {
                errorListener.showError("this is obviously invalid date");
                return;
            }

            int year = date / 100;
            int month = date - year * 100;
            updateCostList(year, month);
            updateViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    /**
     * 選択された費目の有効／無効状態を更新し，表示を最新化する．
     * @param id 更新対象の費目ID
     * @param year 表示最新化の際の検索対象年
     * @param month 表示最新化の際の検索対象月
     */
    public void updateCostStatus(int id, int year, int month)
    {
        try {
            repository.validInvalidCost(id);
            updateCostList(year, month);
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
