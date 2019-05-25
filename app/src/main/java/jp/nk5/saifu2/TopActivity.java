package jp.nk5.saifu2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;

import jp.nk5.saifu2.view.fragment.TopFragment;
import jp.nk5.saifu2.view.fragment.menu.TopMenuFragment;
import jp.nk5.saifu2.view.viewmodel.menu.TopMenu;

public class TopActivity extends BaseActivity implements TopMenuFragment.EventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new TopMenuFragment(), TopMenuFragment.getTagName())
                .replace(R.id.layout_main, new TopFragment(), TopFragment.getTagName())
                .commit();
    }

    public void onClickMenuItem(TopMenu menu)
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        switch (menu)
        {
            case ACCOUNT_BOOK:
                Intent intent = new Intent(this, AccountBookActivity.class);
                startActivity(intent);
                break;
            case SHOP:
                break;
            case BANK:
                intent = new Intent(this, BankActivity.class);
                startActivity(intent);
                break;
        }
    }
}
