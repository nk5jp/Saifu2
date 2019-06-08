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

import java.util.ArrayList;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.CostListAdapter;
import jp.nk5.saifu2.view.viewmodel.CostViewModel;

public class CostFragment extends Fragment implements ListView.OnItemLongClickListener {

    private EventListener listener;
    private CostViewModel viewModel = new CostViewModel(0, 0, new ArrayList<>());
    private View layout;

    public interface EventListener {
        boolean onCostItemLongClick(int position);
    }

    public static String getTagName()
    {
        return "TAG_COST";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_cost, container, false);
        updateView();

        ListView listView = layout.findViewById(R.id.listView1);
        Context context = this.getContext();
        if (context != null)
        {
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

    public CostViewModel getViewModel()
    {
        return this.viewModel;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        return listener.onCostItemLongClick(position);
    }

    public void updateView()
    {
        Context context = this.getContext();
        if (context != null) {
            ListView listView = layout.findViewById(R.id.listView1);
            listView.setAdapter(new CostListAdapter(context, android.R.layout.simple_list_item_1, viewModel.getCosts()));
        }
    }

}
