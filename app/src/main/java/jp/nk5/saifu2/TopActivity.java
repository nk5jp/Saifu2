package jp.nk5.saifu2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import jp.nk5.saifu2.view.TopFragment;
import jp.nk5.saifu2.view.TopMenuFragment;

public class TopActivity extends AppCompatActivity implements TopMenuFragment.EventListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new TopMenuFragment(), TopMenuFragment.getTagName())
                .replace(R.id.layout_main, new TopFragment(), TopFragment.getTagName())
                .commit();
    }

    public void onClickMenuItem(long id)
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

}
