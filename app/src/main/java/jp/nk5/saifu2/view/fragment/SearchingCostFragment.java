package jp.nk5.saifu2.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.nk5.saifu2.R;

public class SearchingCostFragment  extends Fragment {
    public static String getTagName()
    {
        return "TAG_SEARCHING_Cost";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_searching_cost, container, false);
    }
}
