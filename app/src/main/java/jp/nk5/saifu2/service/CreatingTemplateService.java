package jp.nk5.saifu2.service;

import android.content.Context;

import java.util.Optional;

import jp.nk5.saifu2.AccountBookActivity;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.infra.repository.CostRepositorySQLite;
import jp.nk5.saifu2.view.fragment.CreatingTemplateFragment;
import jp.nk5.saifu2.view.fragment.TemplateFragment;
import jp.nk5.saifu2.view.viewmodel.TemplateViewModel;

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
            int id = updateFormViewListener.getViewModel().getId();

            if (id == SpecificId.NotPersisted.getId()) {
                if (isDuplicated(name))
                {
                    errorListener.showError("Name is duplicated.");
                    return;
                }

                repository.setTemplate(name, isControlled);
                updateTemplateList();
                updateInfoViewListener.updateView();
            } else {
                repository.updateTemplate(id, name, isControlled);
                updateTemplateList();
                updateFormViewListener.clearView();
                updateInfoViewListener.updateView();
            }
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
     * 以下の優先順位で選択状態および画面モードを変更して反映する．
     * 選択したものが有効でない場合，何もしない．
     * 選択したものが未選択である場合，そのテンプレートを選択し，画面を編集モードにする
     * 選択したものが選択である場合，そのテンプレートの選択を解除し，画面を追加モードにする
     * @param position 選択したテンプレートリストの行番号
     */
    public void selectTemplate(int position)
    {
        try {
            TemplateViewModel.TemplateForView templateForView = updateInfoViewListener
                    .getViewModel()
                    .getTemplates()
                    .get(position);

            if (!templateForView.getTemplate().isValid()) return;

            if (templateForView.isSelected()) {
                templateForView.setSelected(false);
                updateFormViewListener.clearView();
                updateInfoViewListener.updateView();
            } else {
                int currentSelected = updateFormViewListener.getViewModel().getId();
                unSelectedTemplate(currentSelected);
                templateForView.setSelected(true);
                bindTempalate(templateForView);

                updateFormViewListener.updateView();
                updateInfoViewListener.updateView();
            }
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    /**
     * 指定したIDのテンプレートの有効・無効状態をスイッチし，
     * 画面モデルを最新化して画面表示を更新する．
     * @param id スイッチするテンプレートのID
     */
    public void updateTemplateStatus(int id)
    {
        try {
            repository.validInvalidTemplate(id);
            updateTemplateList();
            updateInfoViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
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

    /**
     * 指定したidに対応するテンプレートの選択を解除する
     * @param id 選択を解除したいテンプレート
     */
    private void unSelectedTemplate(int id) throws Exception
    {
        if (id == SpecificId.NotPersisted.getId()) return;

        Optional<TemplateViewModel.TemplateForView> templateForView
                = updateInfoViewListener.getViewModel().getTemplates().stream()
                .filter(t -> t.getTemplate().getId() == id)
                .findFirst();

        if (templateForView.isPresent())
        {
            templateForView.get().setSelected(false);
        } else {
            throw new Exception();
        }
    }

    /**
     * 選択したテンプレートの情報をフォーム側にバインドする
     * @param templateForView 選択したテンプレート
     */
    private void bindTempalate(TemplateViewModel.TemplateForView templateForView)
    {
        updateFormViewListener.getViewModel().setId(templateForView.getTemplate().getId());
        updateFormViewListener.getViewModel().setName(templateForView.getTemplate().getName());
        updateFormViewListener.getViewModel().setControlled(templateForView.getTemplate().isControlled());
    }


}
