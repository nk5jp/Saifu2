package jp.nk5.saifu2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import jp.nk5.saifu2.view.TopFragment;
import jp.nk5.saifu2.view.TopMenuFragment;
import jp.nk5.saifu2.view.viewmodel.TopMenu;

public class TopActivity extends AppCompatActivity implements TopMenuFragment.EventListener {

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
                break;
            case SHOP:
                break;
            case BANK:
                Intent intent = new Intent(this, BankActivity.class);
                startActivity(intent);
                break;
        }
    }
}
