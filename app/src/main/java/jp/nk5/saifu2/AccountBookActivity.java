package jp.nk5.saifu2;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

import jp.nk5.saifu2.service.CreatingTemplateService;
import jp.nk5.saifu2.service.SearchingCostService;
import jp.nk5.saifu2.view.fragment.CostFragment;
import jp.nk5.saifu2.view.fragment.SearchingCostFragment;
import jp.nk5.saifu2.view.fragment.TemplateFragment;
import jp.nk5.saifu2.view.fragment.menu.AccountBookMenuFragment;
import jp.nk5.saifu2.view.fragment.CreatingTemplateFragment;
import jp.nk5.saifu2.view.viewmodel.menu.AccountBookMenu;

/**
 * Cost画面，Template画面，Extra画面を扱うアクティビティ．
 * 初期表示はCost画面とする．
 */
public class AccountBookActivity extends BaseActivity
        implements AccountBookMenuFragment.EventListener, TemplateFragment.EventListener, CostFragment.EventListener {

    private FragmentManager fragmentManager;

    private TemplateFragment templateFragment;
    private CreatingTemplateFragment creatingTemplateFragment;
    private CostFragment costFragment;

    private CreatingTemplateService creatingTemplateService;
    private SearchingCostService searchingCostService;

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
        templateFragment = new TemplateFragment();
        costFragment = new CostFragment();
        creatingTemplateFragment = new CreatingTemplateFragment();

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
            creatingTemplateService = new CreatingTemplateService(this,
                    creatingTemplateFragment,
                    templateFragment,
                    this
            );
            searchingCostService = new SearchingCostService(this,
                    costFragment,
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
    private void setScreen(AccountBookMenu menu) throws Exception
    {
        switch (menu)
        {
            case COST:
                setSearchingCostScreen();
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
    private void setSearchingCostScreen() throws Exception
    {
        searchingCostService.updateCostList(year, month);
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new AccountBookMenuFragment(), AccountBookMenuFragment.getTagName())
                .replace(R.id.layout_form, new SearchingCostFragment(), SearchingCostFragment.getTagName())
                .replace(R.id.layout_information, costFragment, CostFragment.getTagName())
                .commit();
        currentMenu = AccountBookMenu.COST;
    }

    /**
     * フラグメントの差し替えと，画面モデルの初期化を行う．
     */
    private void setCreatingTemplateScreen() throws Exception
    {
        creatingTemplateService.updateTemplateList();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_menu, new AccountBookMenuFragment(), AccountBookMenuFragment.getTagName())
                .replace(R.id.layout_form, creatingTemplateFragment, CreatingTemplateFragment.getTagName())
                .replace(R.id.layout_information, templateFragment, TemplateFragment.getTagName())
                .commit();
        currentMenu = AccountBookMenu.TEMPLATE;
    }

    /**
     * Template画面のADD/EDIT_TEMPLATEボタン押下時の処理
     * テンプレートを作成もしくは編集し，画面モデルの更新と表示を行う．
     */
    public void onClickAddTemplateButton(View view)
    {
        try {
            String name = getStringFromEditText(R.id.editText1);
            boolean isControlled = isCheckedFromCheckBox(R.id.checkBox1);
            creatingTemplateService.createTemplate(name, isControlled);
        } catch (Exception e) {
            showError("enter NAME");
        }
    }

    /**
     * Cost画面のSEARCHボタン押下時の処理
     * 該当する年月の費目を取得し，画面モデルの更新と表示を行う
     * @param view Cost画面上のSEARCHボタン
     */
    public void onClickSearchButton(View view)
    {
        try {
            int date = getIntFromEditText(R.id.editText1);
            searchingCostService.getSpecificCost(date);
        } catch (Exception e) {
            showError("enter integer with YYYYMM format");
        }
    }

    /**
     * 長押しされた口座を閉塞して表示をアップデートする．
     * @param position 長押しされた口座の行番．
     * @return 継承元のとおり．特に意味はなし．
     */
    public boolean onItemLongClick(int position) {
        int id = templateFragment.getViewModel().getTemplates().get(position).getTemplate().getId();
        creatingTemplateService.updateTemplateStatus(id);
        return true;
    }

    /**
     * ポジションに該当するテンプレートのIDを取得し，サービスに選択処理を依頼する．
     * @param position 選択したリストの行数
     */
    public void onItemClick(int position) {
        try
        {
            creatingTemplateService.selectTemplate(position);
        } catch (Exception e) {
            showError("cannot select.");
        }

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
