package jp.nk5.saifu2.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.AccountListAdapter;
import jp.nk5.saifu2.view.viewmodel.AccountViewModel;

public class AccountFragment extends Fragment implements ListView.OnItemClickListener, ListView.OnItemLongClickListener {

    private EventListener listener;
    private AccountViewModel viewModel = new AccountViewModel(0, new ArrayList<>());
    private View layout;

    public interface EventListener {
        boolean onItemLongClick(int position);
        void onItemClick(int position);
    }

    public static String getTagName()
    {
        return "TAG_ACCOUNT";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_account, container, false);
        updateView();

        ListView listView = layout.findViewById(R.id.listView1);
        Context context = this.getContext();
        if (context != null)
        {
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
        }
        return layout;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            listener = (EventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EventListener");
        }
    }

    public AccountViewModel getViewModel()
    {
        return this.viewModel;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        listener.onItemClick(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        return listener.onItemLongClick(position);
    }

    public void updateView()
    {
        Context context = this.getContext();
        if (context != null) {
            TextView textView = layout.findViewById(R.id.textView1);
            textView.setText(String.format(Locale.JAPAN, "Total: %,d円", viewModel.getSum()));
            ListView listView = layout.findViewById(R.id.listView1);
            listView.setAdapter(new AccountListAdapter(context, android.R.layout.simple_list_item_1, viewModel.getAccounts()));
        }
    }

}
