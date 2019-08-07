package jp.nk5.saifu2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.service.SearchingReceiptService;
import jp.nk5.saifu2.view.fragment.CalendarFragment;
import jp.nk5.saifu2.view.fragment.menu.ShopMenuFragment;
import jp.nk5.saifu2.view.viewmodel.menu.ShopMenu;

public class ShopActivity extends BaseActivity implements ShopMenuFragment.EventListener, CalendarFragment.EventListener {

    private FragmentManager fragmentManager;
    private ShopMenu currentMenu;

    private CalendarFragment calendarFragment;
    private SearchingReceiptService searchingReceiptService;

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
            searchingReceiptService = new SearchingReceiptService(this,
                    calendarFragment,
                    this
            );
        } catch (Exception e) {
            super.onDestroy();
        }
    }

    /**
     * レシート明細画面から戻ってきたときの描画処理
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        try {
            setScreen(currentMenu);
        } catch (Exception e) {
            super.onDestroy();
        }

    }

    /**
     * 指定されたメニューに合わせた画面の初期表示処理を行う
     * @param menu 表示するメニュー
     */
    private void setScreen(ShopMenu menu) throws Exception
    {
        switch (menu)
        {
            case CALENDAR:
                setCalendarScreen();
                break;
            case STATISTICS:
                break;
        }
    }

    /**
     * フラグメントの差し替えと，画面モデルの初期化を行う．
     */
    private void setCalendarScreen() throws Exception
    {
        searchingReceiptService.updateReceiptList();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new ShopMenuFragment(), ShopMenuFragment.getTagName())
                .replace(R.id.layout_main, calendarFragment, CalendarFragment.getTagName())
                .commit();
        calendarFragment.updateView();
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
        MyDate date = searchingReceiptService.getSelectedDate();
        Intent intent = new Intent(this, ReceiptActivity.class);
        intent.putExtra("year", date.getYear());
        intent.putExtra("month", date.getMonth());
        intent.putExtra("day", date.getDay());
        intent.putExtra("id", calendarFragment.getViewModel().getReceipts().get(position).getId());
        startActivity(intent);
    }


    /**
     * 現在選択している日付をintentに格納してレシート詳細画面を起動する．
     * 作成モードなのでIDにはnotPersistedをセットする．
     * @param view 未使用
     */
    public void onClickAddButton(View view)
    {
        MyDate date = searchingReceiptService.getSelectedDate();
        Intent intent = new Intent(this, ReceiptActivity.class);
        intent.putExtra("year", date.getYear());
        intent.putExtra("month", date.getMonth());
        intent.putExtra("day", date.getDay());
        intent.putExtra("id", SpecificId.NotPersisted.getId());
        startActivity(intent);
    }

    public void onSelectedDayChange(int year, int month, int day)
    {
        try {
            searchingReceiptService.selectCalendar(year, month, day);
        } catch (Exception e) {
            showError("cannot select calendar.");
        }
    }
}
