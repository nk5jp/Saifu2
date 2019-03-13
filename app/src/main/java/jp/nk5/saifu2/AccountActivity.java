package jp.nk5.saifu2;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import jp.nk5.saifu2.view.BankMenuFragment;
import jp.nk5.saifu2.view.TopFragment;

public class AccountActivity extends AppCompatActivity implements BankMenuFragment.EventListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new BankMenuFragment(), BankMenuFragment.getTagName())
                .replace(R.id.layout_main, new TopFragment(), TopFragment.getTagName())
                .commit();
    }

    public void onClickMenuItem(long id)
    {
        //エラー回避のための仮実装，あとで消す．
        fragmentManager.findFragmentByTag(TopFragment.getTagName());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
    }

}
