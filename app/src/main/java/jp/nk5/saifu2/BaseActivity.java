package jp.nk5.saifu2;

import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 本アプリケーションで活用する全てのアクティビティの共通メソッドを有する抽象クラス．
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * アクティビティに対して通常メッセージの表示を依頼する
     * @param message 表示対象とする文字列
     */
    public void showMessage (String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

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
     */
    protected int getIntFromEditText (int id)
    {
        EditText view = findViewById(id);
        return Integer.parseInt(view.getText().toString());
    }

    protected boolean isCheckedFromCheckBox (int id)
    {
        CheckBox view = findViewById(id);
        return view.isChecked();
    }

}
