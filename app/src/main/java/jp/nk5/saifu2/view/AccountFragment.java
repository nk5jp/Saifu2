package jp.nk5.saifu2.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.nk5.saifu2.R;

public class AccountFragment extends Fragment {

    public static String getTagName()
    {
        return "TAG_ACCOUNT";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_account, container, false);
        return layout;
    }
}
