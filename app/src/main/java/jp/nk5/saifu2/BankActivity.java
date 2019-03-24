package jp.nk5.saifu2;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import jp.nk5.saifu2.service.OpeningAccountService;
import jp.nk5.saifu2.view.AccountFragment;
import jp.nk5.saifu2.view.BankMenuFragment;
import jp.nk5.saifu2.view.OpeningAccountFragment;
import jp.nk5.saifu2.view.TopFragment;

public class BankActivity extends AppCompatActivity implements BankMenuFragment.EventListener {

    private static OpeningAccountService service;

    public interface UpdateViewListener
    {
        void showError(String message);
        void updateView();
    }

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new BankMenuFragment(), BankMenuFragment.getTagName())
                .replace(R.id.layout_form, new OpeningAccountFragment(), OpeningAccountFragment.getTagName())
                .replace(R.id.layout_information, new AccountFragment(), AccountFragment.getTagName())
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            AccountFragment fragment = (AccountFragment) fragmentManager.findFragmentByTag(AccountFragment.getTagName());
            service = OpeningAccountService.getInstance(this,
                    this,
                    fragment.getViewModel());
        } catch (Exception e) {
            super.onDestroy();
        }
    }

    public void onClickMenuItem(long id)
    {
        //エラー回避のための仮実装，あとで消す．
        fragmentManager.findFragmentByTag(TopFragment.getTagName());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
    }

    public void onClickAddButton(View view)
    {
        service.addAccount("aaa");
    }

    public void showError (String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void updateView()
    {
        AccountFragment fragment = (AccountFragment) fragmentManager.findFragmentByTag(AccountFragment.getTagName());
        fragment.updateView();
    }


}
