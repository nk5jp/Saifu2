package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.AccountBookActivity;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.infra.repository.CostRepositorySQLite;
import jp.nk5.saifu2.view.fragment.CreatingTemplateFragment;
import jp.nk5.saifu2.view.fragment.TemplateFragment;

public class CreatingTemplateService {

    private AccountBookActivity errorListener;
    private CreatingTemplateFragment updateFormViewListener;
    private TemplateFragment updateInfoViewListener;
    private CostRepository repository;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateFormViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param updateInfoViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param errorListener エラー表示の依頼先．実態は依頼元のアクティビティ．
     */
    public CreatingTemplateService (Context context, CreatingTemplateFragment updateFormViewListener, TemplateFragment updateInfoViewListener, AccountBookActivity errorListener) throws Exception
    {
        repository = CostRepositorySQLite.getInstance(context);
        this.updateFormViewListener = updateFormViewListener;
        this.updateInfoViewListener = updateInfoViewListener;
        this.errorListener = errorListener;
    }

    /**
     * 名称の重複がなければテンプレートを永続化し，画面モデルの更新と画面反映を行う
     * @param name 作成するテンプレート名称
     * @param isControlled 作成するテンプレートは見積もり対象である
     */
    public void createTemplate(String name, boolean isControlled)
    {
        try
        {
            if (isDuplicated(name))
            {
                errorListener.showError("Name is duplicated.");
                return;
            }
            repository.setTemplate(name, isControlled);
            updateTemplateList();
            updateInfoViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }


    /**
     * テンプレートを取得し，画面モデルとして加工してセットする．
     * この処理が走った段階でテンプレートの選択状態は解除されることに留意すること．
     */
    public void updateTemplateList() throws Exception
    {
        updateInfoViewListener.getViewModel().transformAndSetTemplates(repository.getAllTemplate());
    }

    /**
     * 指定した名前のテンプレートが既に存在するか確認する
     * @param name 検証したい名称
     * @return 重複している
     */
    private boolean isDuplicated(String name) throws Exception
    {
        return repository.getAllTemplate().stream()
                .anyMatch(a -> a.getName().equals(name));
    }

}
