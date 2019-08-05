package jp.nk5.saifu2;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.nk5.saifu2.adapter.AccountSpinnerAdapter;
import jp.nk5.saifu2.adapter.CostSpinnerAdapter;
import jp.nk5.saifu2.adapter.ReceiptDetailListAdapter;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.Receipt;
import jp.nk5.saifu2.domain.ReceiptDetail;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.service.CreatingReceiptService;
import jp.nk5.saifu2.view.viewmodel.ReceiptDetailViewModel;

public class ReceiptActivity extends BaseActivity implements ListView.OnItemLongClickListener, ListView.OnItemClickListener {

    private ReceiptDetailViewModel viewModel;
    private CreatingReceiptService creatingReceiptService;

    public ReceiptDetailViewModel getViewModel()
    {
        return this.viewModel;
    }

    /**
     * intentとして引き渡された日付パラメータをセットする．
     * @param savedInstanceState 継承元の処理にのみ使用
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
    }

    /**
     * 各サービスのインスタンスを初期生成し，直前に表示していた画面もしくはAccount画面の再表示を行う．
     */
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = this.getIntent();
        int year = intent.getIntExtra("year", 0);
        int month = intent.getIntExtra("month", 0);
        int day = intent.getIntExtra("day", 0);
        int id = intent.getIntExtra("id", SpecificId.NotPersisted.getId());

        if (year * month * day == 0) super.onDestroy();

        ReceiptDetailViewModel.MyMode myMode;
        if (id == SpecificId.NotPersisted.getId()) myMode = ReceiptDetailViewModel.MyMode.ADD;
        else myMode = ReceiptDetailViewModel.MyMode.DELETE;

        viewModel = new ReceiptDetailViewModel(
                id,
                new MyDate(year,month,day),
                0,
                new ArrayList<>(),
                myMode
        );

        try {
            if (viewModel.getMode() == ReceiptDetailViewModel.MyMode.ADD) {
                ListView listView = findViewById(R.id.listView1);
                listView.setOnItemLongClickListener(this);
                listView.setOnItemClickListener(this);
            }
            creatingReceiptService = new CreatingReceiptService(this, this);
            setScreen();
        } catch (Exception e) {
            super.onDestroy();
        }
    }


    /**
     * 画面モデルの初期化を行う．
     */
    private void setScreen() throws Exception
    {
        Spinner accountSpinner = findViewById(R.id.spinner1);
        Spinner costSpinner = findViewById(R.id.spinner2);
        ListView listView = findViewById(R.id.listView1);
        TextView textView = findViewById(R.id.textView1);
        Button button = findViewById(R.id.button2);
        switch (viewModel.getMode()) {
            case ADD:
                costSpinner.setAdapter(
                        new CostSpinnerAdapter(
                                this,
                                android.R.layout.simple_spinner_dropdown_item,
                                creatingReceiptService.getValidCost(viewModel.getDate().getYear(), viewModel.getDate().getMonth())
                        )
                );
                accountSpinner.setAdapter(new AccountSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, creatingReceiptService.getAllValidAccount()));
                break;
            case DELETE:
                Receipt receipt = creatingReceiptService.getReceiptById(viewModel.getId());
                List<ReceiptDetail> details = creatingReceiptService.getReceiptDetailById(viewModel.getId());
                for (ReceiptDetail detail : details)
                {
                    viewModel.addDetail(detail.getCost(), detail.getValue());
                }

                List<Account> accounts = new ArrayList<>();
                accounts.add(receipt.getAccount());
                accountSpinner.setAdapter(
                        new AccountSpinnerAdapter(
                                this,
                                android.R.layout.simple_spinner_dropdown_item,
                                accounts
                        )
                );
                listView.setAdapter(new ReceiptDetailListAdapter(this, android.R.layout.simple_list_item_1, viewModel.getReceiptDetails()));
                textView.setText(String.format(Locale.JAPAN, "SUM: %,d円", viewModel.getSum()));
                button.setText(R.string.str_delete);
        }
    }

    /**
     * 入力内容を踏まえて明細を追加する
     * @param view 使用しない
     */
    public void onClickAddDetailButton(View view)
    {
        try {
            int value = getIntFromEditText(R.id.editText1);
            Spinner costSpinner = findViewById(R.id.spinner2);
            Cost cost = (Cost) costSpinner.getSelectedItem();
            creatingReceiptService.addDetail(cost, value);
        } catch (Exception e) {
            showError("cannot create detail.");
        }
    }

    /**
     * レシートを作成し，口座からお金を引き落とす．
     * 成功したら，口座の残金をダイアログとして表示し，このアクティビティを終了する．
     * @param view 使用しない
     */
    public void onClickAddReceiptButton(View view)
    {
        try {
            switch (viewModel.getMode()) {
                case ADD:
                    Spinner accountSpinner = findViewById(R.id.spinner1);
                    Account account = (Account) accountSpinner.getSelectedItem();
                    int before = account.getBalance();
                    int after = creatingReceiptService.createReceipt(account);

                    new AlertDialog.Builder(this)
                            .setTitle("Result")
                            .setMessage(String.format(Locale.JAPAN, "%s : %,d -> %,d", account.getName(), before, after))
                            .setPositiveButton("OK", null)
                            .show();
                    break;
                case DELETE:
                    creatingReceiptService.deleteReceipt(viewModel.getId());
            }
        } catch (Exception e) {
            showError("cannot create receipt.");
        }
        finish();
    }

    /**
     * リストビューおよび合計値の表示を更新する
     */
    public void updateView()
    {
        ListView listView = findViewById(R.id.listView1);
        listView.setAdapter(new ReceiptDetailListAdapter(this, android.R.layout.simple_list_item_1, viewModel.getReceiptDetails()));
        TextView textView = findViewById(R.id.textView1);
        textView.setText(String.format(Locale.JAPAN, "SUM: %,d円", viewModel.getSum()));
    }

    public void showError(String error)
    {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        creatingReceiptService.deleteDetail(position);
        return true;
    }
}
