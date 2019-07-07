package jp.nk5.saifu2;

import android.os.Bundle;

public class ReceiptActivity extends BaseActivity {

    /**
     * 各フラグメントのインスタンス初期生成を行い，現在メニューを初期設定する．
     * 以後，各フラグメントのインスタンスはプロパティに永続的に保持するものとする．
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

        try {
//            openingAccountService = new OpeningAccountService(this,
//                    accountFragment,
//                    this
//            );
//            transferService = new TransferService(this,
//                    transferFragment,
//                    accountFragment,
//                    this
//            );
              setScreen();
        } catch (Exception e) {
            super.onDestroy();
        }
    }


    /**
     * 画面モデルの初期化を行う．
     */
    private void setScreen()
    {

    }
}
