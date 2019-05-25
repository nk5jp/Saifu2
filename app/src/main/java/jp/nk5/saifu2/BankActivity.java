package jp.nk5.saifu2;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

import jp.nk5.saifu2.service.OpeningAccountService;
import jp.nk5.saifu2.service.SearchingTransferService;
import jp.nk5.saifu2.service.TransferService;
import jp.nk5.saifu2.view.fragment.AccountFragment;
import jp.nk5.saifu2.view.fragment.menu.BankMenuFragment;
import jp.nk5.saifu2.view.fragment.OpeningAccountFragment;
import jp.nk5.saifu2.view.fragment.SearchingTransferFragment;
import jp.nk5.saifu2.view.fragment.TransferFragment;
import jp.nk5.saifu2.view.fragment.TransferHistoryFragment;
import jp.nk5.saifu2.view.viewmodel.menu.BankMenu;

/**
 * Account画面，Transfer画面，Transfer History画面を扱うアクティビティ．
 * 初期表示はAccount画面とする．
 */
public class BankActivity extends BaseActivity
        implements BankMenuFragment.EventListener, AccountFragment.EventListener {

    private OpeningAccountService openingAccountService;
    private TransferService transferService;
    private SearchingTransferService searchingTransferService;
    private AccountFragment accountFragment;
    private TransferFragment transferFragment;
    private TransferHistoryFragment transferHistoryFragment;
    private FragmentManager fragmentManager;
    private BankMenu currentMenu;
    private int year;
    private int month;

    /**
     * 各フラグメントのインスタンス初期生成を行い，現在メニューを初期設定する．
     * 以後，各フラグメントのインスタンスはプロパティに永続的に保持するものとする．
     * @param savedInstanceState 継承元の処理にのみ使用
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);

        fragmentManager = getSupportFragmentManager();
        accountFragment = new AccountFragment();
        transferFragment = new TransferFragment();
        transferHistoryFragment = new TransferHistoryFragment();

        currentMenu = BankMenu.ACCOUNT;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 各サービスのインスタンスを初期生成し，直前に表示していた画面もしくはAccount画面の再表示を行う．
     */
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
            setScreen(currentMenu);
        } catch (Exception e) {
            super.onDestroy();
        }
    }

    /**
     * 指定されたメニューに合わせた画面の初期表示処理を行う
     * @param menu 表示するメニュー
     */
    private void setScreen(BankMenu menu) throws Exception
    {
        switch (menu)
        {
            case ACCOUNT:
                setOpeningAccountScreen();
                break;
            case TRANSFER:
                setTransferScreen();
                break;
            case TRANSFER_HISTORY:
                setTransferHistoryScreen();
                break;
        }
    }

    /**
     * フラグメントの差し替えと，画面モデルの初期化を行う．
     */
    private void setOpeningAccountScreen() throws Exception
    {
        openingAccountService.updateAccountList();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new BankMenuFragment(), BankMenuFragment.getTagName())
                .replace(R.id.layout_form, new OpeningAccountFragment(), OpeningAccountFragment.getTagName())
                .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                .commit();
        currentMenu = BankMenu.ACCOUNT;
    }

    /**
     * フラグメントの差し替えと，画面モデルの初期化を行う．
     */
    private void setTransferScreen() throws Exception
    {
        openingAccountService.updateAccountList();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_form, transferFragment, TransferFragment.getTagName())
                .replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                .commit();
        currentMenu = BankMenu.TRANSFER;
    }

    /**
     * フラグメントの差し替えと画面モデルの初期化を行う．
     */
    private void setTransferHistoryScreen() throws Exception
    {
        searchingTransferService.updateTransferList(year, month);
        fragmentManager.beginTransaction()
                .replace(R.id.layout_form, new SearchingTransferFragment(), SearchingTransferFragment.getTagName())
                .replace(R.id.layout_information, transferHistoryFragment, TransferHistoryFragment.getTagName())
                .commit();
        currentMenu = BankMenu.TRANSFER_HISTORY;
    }

    /**
     * ドロワーを閉じた上で，ドロワー上で選択した画面に遷移する
     * @param menu 選択した画面
     */
    public void onClickMenuItem(BankMenu menu)
    {
        try {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawers();
            setScreen(menu);
        } catch (Exception e) {
            showError("viewModel cannot updated");
        }

    }

    /**
     * 入力した名称で口座を作成し，リストをアップデートする
     * @param view Account画面上のAddボタン
     */
    public void onClickAddButton(View view)
    {
        try {
            String name = getStringFromEditText(R.id.editText1);
            openingAccountService.addAccount(name);
        } catch (Exception e) {
            showError("enter NAME");
        }
    }

    /**
     * 画面の指定内容に応じて入金もしくは振替を行う
     * @param view Transfer画面上のTransferボタン
     */
    public void onClickTransferButton(View view)
    {
        try {
            int value = getIntFromEditText(R.id.editText1);
            transferService.transferMoney(value);
        } catch (Exception e) {
            showError("enter price");
        }
    }

    /**
     * 画面の指定内容をリセットし，表示をアップデートする
     * @param view Transfer画面上のResetボタン
     */
    public void onClickResetButton(View view)
    {
        transferService.resetTransferAccount();
    }

    /**
     * 指定した年月に該当する入金もしくは振替履歴を表示する
     * @param view TransferHistory画面上のSearchボタン
     */
    public void onClickSearchButton(View view)
    {
        try {
            int date = getIntFromEditText(R.id.editText1);
            searchingTransferService.getSpecificTransfer(date);
        } catch (Exception e) {
            showError("enter integer with YYYYMM format");
        }
    }

    /**
     * 長押しされた口座を閉塞して表示をアップデートする．
     * これはAccount画面のみ可能な操作とする．
     * @param position 長押しされた口座の行番．
     * @return 継承元のとおり．特に意味はなし．
     */
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

    /**
     * 口座の選択に応じて画面モデルおよび表示をアップデートする．
     * これはTransfer画面のみ可能な操作とする．
     * @param position 選択された口座の行番．
     */
    @Override
    public void onItemClick(int position) {
        if (currentMenu == BankMenu.TRANSFER)
        {
            int id = accountFragment.getViewModel().getAccounts().get(position).getId();
            transferService.setTransferAccount(id);
        }
    }

}
