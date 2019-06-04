package jp.nk5.saifu2.service;

import android.content.Context;

import java.util.List;

import jp.nk5.saifu2.AccountBookActivity;
import jp.nk5.saifu2.domain.Template;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.infra.repository.CostRepositorySQLite;
import jp.nk5.saifu2.view.fragment.CostFragment;

public class CreatingCostService {

    private CostRepository repository;
    private CostFragment updateViewListener;
    private AccountBookActivity errorListener;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param errorListener エラー表示の依頼先．実態は依頼元のアクティビティ．
     */
    public CreatingCostService(Context context, CostFragment updateViewListener, AccountBookActivity errorListener) throws Exception
    {
        this.repository = CostRepositorySQLite.getInstance(context);
        this.updateViewListener = updateViewListener;
        this.errorListener = errorListener;
    }


    /**
     * 対象年月のコストを取得し，Apply済か否かを確認する．
     * 未Applyの場合，有効なテンプレートからコストを一括作成する．
     * @param year 生成対象年
     * @param month 生成対象月
     */
    public void createCostFromTemplate(int year, int month)
    {
        try {
            if (isAlreadyApplied(year, month)) {
                errorListener.showError("already applied.");
                return;
            }

            List<Template> templates = repository.getAllTemplate();
            for(Template template : templates)
            {
                if (template.isValid()) repository.setCostFromTemplate(year, month, template);
            }
            errorListener.showMessage("template applied.");

        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    /**
     *
     * @param year 対象となる年
     * @param month 対象となる月
     * @return テンプレートから作られた費目が存在する
     */
    private boolean isAlreadyApplied(int year, int month) throws Exception
    {
        return repository.getSpecificCost(year, month).stream()
                .anyMatch(c -> c.getTemplateId() != SpecificId.MeansNull.getId());
    }

}
