package jp.nk5.saifu2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.Locale;

import jp.nk5.saifu2.domain.Account;
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
                    topFragment
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
        try {
            if (executingShortcutService.isShopCase(position)) {
                Account account = executingShortcutService.getAccount(position, 0);
                int beforeBalance = account.getBalance();
                int afterBalance = executingShortcutService.createReceipt(position);
                new AlertDialog.Builder(this)
                        .setTitle("Result")
                        .setMessage(String.format(Locale.JAPAN, "%s : %,d -> %,d", account.getName(), beforeBalance, afterBalance))
                        .setPositiveButton("OK", null)
                        .show();
            } else if (executingShortcutService.isTransferCase(position)) {
                Account toAccount = executingShortcutService.getAccount(position, 0);
                int beforeToBalance = toAccount.getBalance();
                Account fromAccount = executingShortcutService.getAccount(position, 1);
                int beforeFromBalance = fromAccount.getBalance();

                int value = executingShortcutService.transferMoney(position);
                new AlertDialog.Builder(this)
                        .setTitle("Result")
                        .setMessage(String.format(Locale.JAPAN, "%s : %,d -> %,d" + "\n" + "%s : %,d -> %,d", toAccount.getName(), beforeToBalance, beforeToBalance + value,
                                fromAccount.getName(), beforeFromBalance, beforeFromBalance - value))
                        .setPositiveButton("OK", null)
                        .show();
            }
        } catch (Exception e) {
            showError("cannot execute shortcut.");
        }
    }

}
