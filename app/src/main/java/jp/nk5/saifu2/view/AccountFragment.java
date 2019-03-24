package jp.nk5.saifu2.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.AccountListAdapter;
import jp.nk5.saifu2.view.viewmodel.AccountViewModel;

public class AccountFragment extends Fragment {

    private AccountViewModel viewModel;
    private View layout;

    public static String getTagName()
    {
        return "TAG_ACCOUNT";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        viewModel = new AccountViewModel(new ArrayList<>());
        layout = inflater.inflate(R.layout.fragment_account, container, false);
        return layout;
    }

    public AccountViewModel getViewModel()
    {
        return this.viewModel;
    }

    public void updateView()
    {
        Context context = this.getContext();
        if (context != null) {
            ListView listView = layout.findViewById(R.id.listView1);
            listView.setAdapter(new AccountListAdapter(context, android.R.layout.simple_list_item_1, viewModel.getAccounts()));
        }
    }

}
