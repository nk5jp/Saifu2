package jp.nk5.saifu2.view.fragment.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.MenuListAdapter;
import jp.nk5.saifu2.view.viewmodel.menu.AccountBookMenu;

/**
 * AccountBook画面におけるメニューを扱うためのFragment
 */
public class AccountBookMenuFragment extends Fragment implements ListView.OnItemClickListener {

    private EventListener listener;

    /**
     * タグ付け用に用いる．replaceのタイミングで使用している．結局使わない？
     * @return このFragmentを意味する文字列．
     */
    public static String getTagName () {
        return "TAG_ACCOUNT_BOOK_MENU";
    }

    /**
     * メニューアイテムの選択時の処理をActivityに移譲するために用いるインターフェース
     */
    public interface EventListener {
        void onClickMenuItem(AccountBookMenu menu);
    }

    /**
     * レイアウト作成時に，リストビューの選択処理をこれ自身に移譲する
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_menu, container, false);

        ListView listView = layout.findViewById(R.id.listView1);
        Context context = this.getContext();
        if (context != null)
        {
            listView.setAdapter(new MenuListAdapter(context, android.R.layout.simple_list_item_1, AccountBookMenu.values()));
            listView.setOnItemClickListener(this);
        }
        return layout;
    }

    /**
     * Activityに貼り付けられたタイミングでリスナーを設定する
     * @param context 貼り付け先のActivity
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            listener = (EventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MenuFragmentListener");
        }
    }

    /**
     * メニュー選択時の処理．リスナー側に移譲するだけ
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AccountBookMenu menu = (AccountBookMenu) parent.getItemAtPosition(position);
        listener.onClickMenuItem(menu);
    }

}
