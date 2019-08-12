package jp.nk5.saifu2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.TransferListAdapter;
import jp.nk5.saifu2.view.viewmodel.TransferHistoryViewModel;

public class TransferHistoryFragment extends Fragment {

    private TransferHistoryViewModel viewModel = new TransferHistoryViewModel(new ArrayList<>());
    private View layout;

    public static String getTagName()
    {
        return "TAG_TRANSFER_HISTORY";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_transfer_history, container, false);
        updateView();
        return layout;
    }

    public TransferHistoryViewModel getViewModel()
    {
        return this.viewModel;
    }

    public void updateView()
    {
        Context context = this.getContext();
        if (context != null) {
            ListView listView = layout.findViewById(R.id.listView1);
            listView.setAdapter(new TransferListAdapter(context, android.R.layout.simple_list_item_1, viewModel.getTransfers()));
        }
    }

}
