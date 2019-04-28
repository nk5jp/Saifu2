package jp.nk5.saifu2;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import jp.nk5.saifu2.service.OpeningAccountService;
import jp.nk5.saifu2.view.AccountFragment;
import jp.nk5.saifu2.view.BankMenuFragment;
import jp.nk5.saifu2.view.OpeningAccountFragment;
import jp.nk5.saifu2.view.TransferFragment;
import jp.nk5.saifu2.view.viewmodel.BankMenu;

public class BankActivity extends AppCompatActivity implements BankMenuFragment.EventListener, ListView.OnItemLongClickListener {

    private static OpeningAccountService service;
    private AccountFragment accountFragment;
    private FragmentManager fragmentManager;
    private BankMenu currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        fragmentManager = getSupportFragmentManager();
        accountFragment = new AccountFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new BankMenuFragment(), BankMenuFragment.getTagName())
                .replace(R.id.layout_form, new OpeningAccountFragment(), OpeningAccountFragment.getTagName())
                .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            AccountFragment fragment = (AccountFragment) fragmentManager.findFragmentByTag(AccountFragment.getTagName());
            if (fragment != null)
            {
                service = new OpeningAccountService(this,
                        this,
                        fragment.getViewModel());
                service.getAllAccount();
                ListView listView = findViewById(R.id.listView1);
                listView.setOnItemLongClickListener(this);
                currentMenu = BankMenu.ACCOUNT;
            }
        } catch (Exception e) {
            super.onDestroy();
        }
    }

    public void onClickMenuItem(BankMenu menu)
    {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        switch (menu)
        {
            case ACCOUNT:
                fragmentManager.beginTransaction()
                        .replace(R.id.layout_form, new OpeningAccountFragment(), OpeningAccountFragment.getTagName())
                        .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                        .commit();
                currentMenu = BankMenu.ACCOUNT;
                break;
            case TRANSFER:
                fragmentManager.beginTransaction()
                        .replace(R.id.layout_form, new TransferFragment(), TransferFragment.getTagName())
                        .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                        .commit();
                currentMenu = BankMenu.TRANSFER;
                break;
            case TRANSFER_HISTORY:
                break;
        }
    }

    public void onClickAddButton(View view)
    {
        try {
            String name = getStringFromTextView(R.id.editText1);
            service.addAccount(name);
        } catch (Exception e) {
            showError("enter NAME");
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long i) {
        if (currentMenu == BankMenu.ACCOUNT)
        {
            AccountFragment fragment = (AccountFragment) fragmentManager.findFragmentByTag(AccountFragment.getTagName());
            if (fragment != null) {
                int id = fragment.getViewModel().getAccounts().get(position).getId();
                service.updateAccountStatus(id);
                return true;
            }
        }
        return false;
    }

    public void showError (String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void updateView()
    {
        AccountFragment fragment = (AccountFragment) fragmentManager.findFragmentByTag(AccountFragment.getTagName());
        if (fragment != null) {
            fragment.updateView();
        } else {
            showError("failed to find AccountFragment");
        }
    }

    private String getStringFromTextView (int id) throws Exception
    {
        EditText view = findViewById(id);
        String text = view.getText().toString();
        if (text.equals("")) throw new Exception();
        return text;
    }
}
