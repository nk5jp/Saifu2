package jp.nk5.saifu2.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.AccountListAdapter;
import jp.nk5.saifu2.view.viewmodel.AccountViewModel;

public class TemplateFragment extends Fragment implements ListView.OnItemClickListener, ListView.OnItemLongClickListener {

    private EventListener listener;
    private AccountViewModel viewModel = new AccountViewModel(new ArrayList<>());
    private View layout;

    public interface EventListener {
        boolean onItemLongClick(int position);
        void onItemClick(int position);
    }

    public static String getTagName()
    {
        return "TAG_TEMPLATE";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_template, container, false);
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
            ListView listView = layout.findViewById(R.id.listView1);
            listView.setAdapter(new AccountListAdapter(context, android.R.layout.simple_list_item_1, viewModel.getAccounts()));
        }
    }

}
