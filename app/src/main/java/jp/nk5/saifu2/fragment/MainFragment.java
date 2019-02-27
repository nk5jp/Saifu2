package jp.nk5.saifu2.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.nk5.saifu2.R;


public class MainFragment extends Fragment {

    public static String getTagName()
    {
        return "TAG_MAIN";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_main, container, false);
        return layout;
    }
}
