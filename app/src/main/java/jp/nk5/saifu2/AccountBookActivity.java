package jp.nk5.saifu2;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;

import java.util.Calendar;

import jp.nk5.saifu2.view.fragment.menu.AccountBookMenuFragment;
import jp.nk5.saifu2.view.fragment.CreatingTemplateFragment;
import jp.nk5.saifu2.view.viewmodel.menu.AccountBookMenu;

/**
 * Account画面，Transfer画面，Transfer History画面を扱うアクティビティ．
 * 初期表示はAccount画面とする．
 */
public class AccountBookActivity extends BaseActivity
        implements AccountBookMenuFragment.EventListener {

    private FragmentManager fragmentManager;
    private AccountBookMenu currentMenu;
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
        //accountFragment = new AccountFragment();

        currentMenu = AccountBookMenu.COST;
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
            //openingAccountService = new OpeningAccountService(this,
            //        accountFragment,
            //        this
            //);
            setScreen(currentMenu);
        } catch (Exception e) {
            super.onDestroy();
        }
    }

    /**
     * 指定されたメニューに合わせた画面の初期表示処理を行う
     * @param menu 表示するメニュー
     */
    private void setScreen(AccountBookMenu menu)
    {
        switch (menu)
        {
            case COST:
                setSearcingCostScreen();
                break;
            case TEMPLATE:
                setCreatingTemplateScreen();
                break;
            case EXTRA:
                break;
        }
    }

    /**
     * フラグメントの差し替えと，画面モデルの初期化を行う．
     */
    private void setSearcingCostScreen()
    {
        //openingAccountService.updateAccountList();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new AccountBookMenuFragment(), AccountBookMenuFragment.getTagName())
                //.replace(R.id.layout_form, new OpeningAccountFragment(), OpeningAccountFragment.getTagName())
                //.replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                .commit();
        currentMenu = AccountBookMenu.COST;
    }

    /**
     * フラグメントの差し替えと，画面モデルの初期化を行う．
     */
    private void setCreatingTemplateScreen()
    {
        //openingAccountService.updateAccountList();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new AccountBookMenuFragment(), AccountBookMenuFragment.getTagName())
                .replace(R.id.layout_form, new CreatingTemplateFragment(), CreatingTemplateFragment.getTagName())
                //.replace(R.id.layout_information, accountFragment, AccountFragment.getTagName())
                .commit();
        currentMenu = AccountBookMenu.COST;
    }

    /**
     * ドロワーを閉じた上で，ドロワー上で選択した画面に遷移する
     * @param menu 選択した画面
     */
    public void onClickMenuItem(AccountBookMenu menu)
    {
        try {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawers();
            setScreen(menu);
        } catch (Exception e) {
            showError("viewModel cannot updated");
        }
    }
}
