package jp.nk5.saifu2.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.nk5.saifu2.R;

public class OpeningAccountFragment extends Fragment {
    public static String getTagName()
    {
        return "TAG_OPENING_ACCOUNT";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_opening_account, container, false);
    }
}
