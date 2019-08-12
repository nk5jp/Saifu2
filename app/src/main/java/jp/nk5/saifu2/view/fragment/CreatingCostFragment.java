package jp.nk5.saifu2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import jp.nk5.saifu2.R;

public class CreatingCostFragment extends Fragment {

    private View layout;

    public static String getTagName()
    {
        return "TAG_CREATING_COST";
    }

    /**
     * replaceのたびに画面モデルを初期化する
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_creating_cost, container, false);
        return layout;
    }

    /**
     * 各情報をクリアする，画面表示まで行う
     */
    public void clearView()
    {
        EditText editText = layout.findViewById(R.id.editText1);
        editText.setText("");
        editText = layout.findViewById(R.id.editText1);
        editText.setText("");
    }

}
