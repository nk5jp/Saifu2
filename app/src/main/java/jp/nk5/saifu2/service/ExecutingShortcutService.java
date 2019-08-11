package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.TopActivity;
import jp.nk5.saifu2.domain.Shortcut;
import jp.nk5.saifu2.domain.repository.ShortcutRepository;
import jp.nk5.saifu2.infra.repository.ShortcutRepositorySQLite;
import jp.nk5.saifu2.view.fragment.TopFragment;

public class ExecutingShortcutService {

    private TopActivity errorListener;
    private TopFragment updateViewListener;
    private ShortcutRepository shortcutRepository;

    /**
     *
     * @param context 依頼元のアクティビティ．リポジトリの初期化の都合上必要であり本クラスでは使用しない．
     * @param updateViewListener 画面モデルおよび表示更新の依頼先．実態は依頼元のフラグメント．
     * @param errorListener エラー表示の依頼先．実態は依頼元のアクティビティ．
     */
    public ExecutingShortcutService(Context context, TopFragment updateViewListener, TopActivity errorListener) throws Exception
    {
        this.shortcutRepository = ShortcutRepositorySQLite.getInstance(context);
        this.updateViewListener = updateViewListener;
        this.errorListener = errorListener;
    }

    public void updateShortcutArray() throws Exception
    {
        Shortcut[] shortcuts = updateViewListener.getViewModel().getShortcuts();
        for (int i = 0; i < 6; i++) shortcuts[i] = shortcutRepository.getShortcutByBoxId(i);
    }




}
