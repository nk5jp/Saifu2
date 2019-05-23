package jp.nk5.saifu2;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 本アプリケーションで活用する全てのアクティビティの共通メソッドを有する抽象クラス．
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * アクティビティに対してエラーメッセージの表示を依頼する
     * @param message 表示対象とする文字列
     */
    public void showError (String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Viewから文字列を取得する
     * @param id ViewのID
     * @return そのViewに格納されている文字列
     * @throws Exception viewが見つからない場合，もしくはテキストが空文字である場合
     */
    protected String getStringFromEditText (int id) throws Exception
    {
        EditText view = findViewById(id);
        String text = view.getText().toString();
        if (text.equals("")) throw new Exception();
        return text;
    }

    /**
     * Viewから数字を取得する
     * @param id ViewのID
     * @return そのViewに格納されている数字
     * @throws Exception viewが見つからない場合，もしくは値の整数化に失敗した場合
     */
    protected int getIntFromEditText (int id) throws Exception
    {
        EditText view = findViewById(id);
        return Integer.parseInt(view.getText().toString());
    }

}
