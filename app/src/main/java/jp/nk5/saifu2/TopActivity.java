package jp.nk5.saifu2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import jp.nk5.saifu2.service.ExecutingShortcutService;
import jp.nk5.saifu2.view.fragment.TopFragment;
import jp.nk5.saifu2.view.fragment.menu.TopMenuFragment;
import jp.nk5.saifu2.view.viewmodel.menu.TopMenu;

public class TopActivity extends BaseActivity implements TopMenuFragment.EventListener, TopFragment.EventListener {

    private ExecutingShortcutService executingShortcutService;
    private TopFragment topFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        topFragment = new TopFragment();
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            executingShortcutService = new ExecutingShortcutService(
                    this,
                    topFragment,
                    this
            );
        } catch (Exception e) {
            super.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            setScreen();
        } catch (Exception e) {
            super.onDestroy();
        }
    }

    public void setScreen() throws Exception
    {
        executingShortcutService.updateShortcutArray();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new TopMenuFragment(), TopMenuFragment.getTagName())
                .replace(R.id.layout_main, topFragment, TopFragment.getTagName())
                .commit();
        topFragment.updateView();
    }

    public void onClickMenuItem(TopMenu menu)
    {
        switch (menu)
        {
            case ACCOUNT_BOOK:
                Intent intent = new Intent(this, AccountBookActivity.class);
                startActivity(intent);
                break;
            case SHOP:
                intent = new Intent(this, ShopActivity.class);
                startActivity(intent);
                break;
            case BANK:
                intent = new Intent(this, BankActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void onItemClick(int position)
    {
        Intent intent = new Intent(this, ShortcutActivity.class);
        intent.putExtra("boxId", position);
        startActivity(intent);
    }

    public void onItemLongClick(int position)
    {
        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

}
