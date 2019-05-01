package jp.nk5.saifu2;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.nk5.saifu2.service.OpeningAccountService;
import jp.nk5.saifu2.service.SearchingTransferService;
import jp.nk5.saifu2.service.TransferService;
import jp.nk5.saifu2.view.AccountFragment;
import jp.nk5.saifu2.view.BankMenuFragment;
import jp.nk5.saifu2.view.OpeningAccountFragment;
import jp.nk5.saifu2.view.SearchingTransferFragment;
import jp.nk5.saifu2.view.TransferFragment;
import jp.nk5.saifu2.view.TransferHistoryFragment;
import jp.nk5.saifu2.view.viewmodel.BankMenu;

public class BankActivity extends AppCompatActivity
        implements BankMenuFragment.EventListener, AccountFragment.EventListener {

    private OpeningAccountService openingAccountService;
    private TransferService transferService;
    private SearchingTransferService searchingTransferService;
    private AccountFragment accountFragment;
    private TransferFragment transferFragment;
    private TransferHistoryFragment transferHistoryFragment;
    private FragmentManager fragmentManager;
    private BankMenu currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        fragmentManager = getSupportFragmentManager();
        accountFragment = new AccountFragment();
        transferFragment = new TransferFragment();
        transferHistoryFragment = new TransferHistoryFragment();

    }

    @Override
    protected void onStart() {
        super.onStart();

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
            searchingTransferService = new SearchingTransferService(this,
                    transferHistoryFragment,
                    this
            );
            openingAccountService.getAllAccount();

            fragmentManager.beginTransaction()
                    .replace(R.id.layout_menu, new BankMenuFragment(), BankMenuFragment.getTagName())
                    .replace(R.id.layout_form, new OpeningAccountFragment(), OpeningAccountFragment.getTagName())
                    .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                    .commit();
            currentMenu = BankMenu.ACCOUNT;

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
                openingAccountService.getAllAccount();
                fragmentManager.beginTransaction()
                        .replace(R.id.layout_form, new OpeningAccountFragment(), OpeningAccountFragment.getTagName())
                        .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                        .commit();
                currentMenu = BankMenu.ACCOUNT;
                break;
            case TRANSFER:
                openingAccountService.getAllAccount();
                fragmentManager.beginTransaction()
                        .replace(R.id.layout_form, transferFragment, TransferFragment.getTagName())
                        .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                        .commit();
                currentMenu = BankMenu.TRANSFER;
                break;
            case TRANSFER_HISTORY:
                searchingTransferService.getAllTransfer();
                fragmentManager.beginTransaction()
                        .replace(R.id.layout_form, new SearchingTransferFragment(), SearchingTransferFragment.getTagName())
                        .replace(R.id.layout_information, transferHistoryFragment, TransferHistoryFragment.getTagName())
                        .commit();
                currentMenu = BankMenu.TRANSFER;
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
            int value = getIntFromEditText(R.id.editText1);
            transferService.transferMoney(value);
        } catch (Exception e) {
            showError("enter price");
        }
    }

    public void onClickResetButton(View view)
    {
        transferService.resetTransferAccount();
    }

    public void onClickSearchButton(View view)
    {
        try {
            int date = getIntFromEditText(R.id.editText1);
            searchingTransferService.getSpecificTransfer(date);
        } catch (Exception e) {
            showError("enter integer with YYYYMM format");
        }
    }

    @Override
    public boolean onItemLongClick(int position) {
        if (currentMenu == BankMenu.ACCOUNT)
        {
            int id = accountFragment.getViewModel().getAccounts().get(position).getId();
            openingAccountService.updateAccountStatus(id);
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(int position) {
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

    private int getIntFromEditText (int id) throws Exception
    {
            EditText view = findViewById(id);
            return Integer.parseInt(view.getText().toString());
    }

}
