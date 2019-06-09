package jp.nk5.saifu2;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;

import jp.nk5.saifu2.view.fragment.CalendarFragment;
import jp.nk5.saifu2.view.fragment.menu.ShopMenuFragment;
import jp.nk5.saifu2.view.viewmodel.menu.ShopMenu;

public class ShopActivity extends BaseActivity implements ShopMenuFragment.EventListener, CalendarFragment.EventListener {

    private FragmentManager fragmentManager;
    private ShopMenu currentMenu;

    private CalendarFragment calendarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        fragmentManager = getSupportFragmentManager();

        calendarFragment = new CalendarFragment();

        currentMenu = ShopMenu.CALENDAR;
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
//            openingAccountService = new OpeningAccountService(this,
//                    accountFragment,
//                    this
//            );
            setScreen(currentMenu);
        } catch (Exception e) {
            super.onDestroy();
        }
    }

    /**
     * 指定されたメニューに合わせた画面の初期表示処理を行う
     * @param menu 表示するメニュー
     */
    private void setScreen(ShopMenu menu)
    {
        switch (menu)
        {
            case CALENDAR:
                setCalendarScreen();
                break;
            case STATISTICS:
                break;
            case TAX:
                break;
        }
    }

    /**
     * フラグメントの差し替えと，画面モデルの初期化を行う．
     */
    private void setCalendarScreen()
    {
//        openingAccountService.updateAccountList();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new ShopMenuFragment(), ShopMenuFragment.getTagName())
                .replace(R.id.layout_main, calendarFragment, CalendarFragment.getTagName())
                .commit();
        currentMenu = ShopMenu.CALENDAR;
    }

    /**
     * ドロワーを閉じた上で，ドロワー上で選択した画面に遷移する
     * @param menu 選択した画面
     */
    public void onClickMenuItem(ShopMenu menu)
    {
        try {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawers();
            setScreen(menu);
        } catch (Exception e) {
            showError("viewModel cannot updated");
        }
    }

    public void onItemClick(int position)
    {

    }


}
