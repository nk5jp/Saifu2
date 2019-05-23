package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.BankActivity;
import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.infra.AccountRepositorySQLite;
import jp.nk5.saifu2.view.AccountFragment;

/**
 * 口座の開設・閉塞に関する処理を担う．
 */
public class OpeningAccountService {

    private BankActivity errorListener;
    private AccountFragment updateViewListener;
    private AccountRepository repository;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param errorListener エラー表示の依頼先．実態は依頼元のアクティビティ．
     * @throws Exception 初期化にかかる各種エラーが発生した場合
     */
    public OpeningAccountService(Context context, AccountFragment updateViewListener, BankActivity errorListener) throws Exception
    {
        this.repository = AccountRepositorySQLite.getInstance(context);
        this.updateViewListener = updateViewListener;
        this.errorListener = errorListener;
    }

    /**
     * Account画面のAddボタン押下時の処理．
     * 口座名に問題が無ければ永続化し，画面モデルを最新化して画面表示を更新する．
     * @param name 開設したい口座名
     */
    public void addAccount(String name)
    {
        try
        {
            if (isDuplicated(name)) {
                errorListener.showError("Name is duplicated.");
                return;
            }
            repository.setAccount(name);
            updateAccountList();
            updateViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    /**
     * 画面モデルの口座リストを最新化する．これ自体が表示更新処理は担っていない点に注意．
     */
    public void updateAccountList() throws Exception
    {
        updateViewListener.getViewModel().setAccounts(repository.getAllAccount());
    }

    /**
     * 指定したIDの口座の開設・閉塞状態をスイッチし，
     * 画面モデルを最新化して画面表示を更新する．
     * @param id スイッチする口座のID
     */
    public void updateAccountStatus(int id)
    {
        try {
            repository.openCloseAccount(id);
           updateAccountList();
            updateViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    /**
     * 指定した名前の口座が既に存在するか確認する
     * @param name 検証したい名称
     * @return 重複している
     * @throws Exception 検証における各種処理でエラーが発生した場合
     */
    private boolean isDuplicated(String name) throws Exception
    {
            return repository.getAllAccount().stream()
                    .filter(a -> a.getName().equals(name))
                    .count() != 0;
    }
}
