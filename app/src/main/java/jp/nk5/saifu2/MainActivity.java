package jp.nk5.saifu2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import jp.nk5.saifu2.fragment.MainFragment;
import jp.nk5.saifu2.fragment.MenuFragment;

public class MainActivity extends AppCompatActivity implements MenuFragment.EventListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new MenuFragment(), MenuFragment.getTagName())
                .replace(R.id.layout_main, new MainFragment(), MainFragment.getTagName())
                .commit();
    }

    public void onClickMenuItem(long id)
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        Intent intent = new Intent(this, BankActivity.class);
        startActivity(intent);
    }

}
