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
import jp.nk5.saifu2.service.TransferService;
import jp.nk5.saifu2.view.AccountFragment;
import jp.nk5.saifu2.view.BankMenuFragment;
import jp.nk5.saifu2.view.OpeningAccountFragment;
import jp.nk5.saifu2.view.TransferFragment;
import jp.nk5.saifu2.view.viewmodel.BankMenu;

public class BankActivity extends AppCompatActivity
        implements BankMenuFragment.EventListener, ListView.OnItemLongClickListener, ListView.OnItemClickListener {

    private OpeningAccountService openingAccountService;
    private TransferService transferService;
    private AccountFragment accountFragment;
    private TransferFragment transferFragment;
    private FragmentManager fragmentManager;
    private BankMenu currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        fragmentManager = getSupportFragmentManager();
        accountFragment = new AccountFragment();
        transferFragment = new TransferFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new BankMenuFragment(), BankMenuFragment.getTagName())
                .replace(R.id.layout_form, new OpeningAccountFragment(), OpeningAccountFragment.getTagName())
                .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                .commit();
        currentMenu = BankMenu.ACCOUNT;
    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView listView = findViewById(R.id.listView1);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);

        try {
            openingAccountService = new OpeningAccountService(this,
                    accountFragment,
                    this
            );
            transferService = new TransferService(this,
                    transferFragment,
                    accountFragment,
                    this
            );
            openingAccountService.getAllAccount();
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
                        .replace(R.id.layout_form, transferFragment, TransferFragment.getTagName())
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
            String name = getStringFromEditText(R.id.editText1);
            openingAccountService.addAccount(name);
        } catch (Exception e) {
            showError("enter NAME");
        }
    }

    public void onClickTransferButton(View view)
    {
        try {
            int value = getIntFromEditText(R.id.editText1, 0);
            if (value == 0) return;
            transferService.transferMoney(value);
        } catch (Exception e) {
            showError("enter price");
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long i) {
        if (currentMenu == BankMenu.ACCOUNT)
        {
            int id = accountFragment.getViewModel().getAccounts().get(position).getId();
            openingAccountService.updateAccountStatus(id);
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long i) {
        if (currentMenu == BankMenu.TRANSFER)
        {
            int id = accountFragment.getViewModel().getAccounts().get(position).getId();
            transferService.setTransferAccount(id);
        }
    }

    public void showError (String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String getStringFromEditText (int id) throws Exception
    {
        EditText view = findViewById(id);
        String text = view.getText().toString();
        if (text.equals("")) throw new Exception();
        return text;
    }

    private int getIntFromEditText (int id, int defaultValue) throws Exception
    {
        int value;
        try {
            EditText view = findViewById(id);
            value = Integer.parseInt(view.getText().toString());
        } catch (Exception e) {
            value = defaultValue;
        }
        return value;
    }

}
