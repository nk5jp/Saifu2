package jp.nk5.saifu2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.ReceiptListAdapter;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.view.viewmodel.CalendarViewModel;

public class CalendarFragment extends Fragment implements ListView.OnItemClickListener {

    private EventListener listener;
    private CalendarViewModel viewModel = new CalendarViewModel(new MyDate(1, 1), new ArrayList<>());
    private View layout;

    public interface EventListener {
        void onItemClick(int position);
    }

    public static String getTagName()
    {
        return "TAG_CALENDAR";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_calendar, container, false);
        updateView();

        ListView listView = layout.findViewById(R.id.listView1);
        Context context = this.getContext();
        if (context != null)
        {
            listView.setOnItemClickListener(this);
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

    public CalendarViewModel getViewModel()
    {
        return this.viewModel;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        listener.onItemClick(position);
    }

    public void updateView()
    {
        Context context = this.getContext();
        if (context != null) {
            ListView listView = layout.findViewById(R.id.listView1);
            listView.setAdapter(new ReceiptListAdapter(context, android.R.layout.simple_list_item_1, viewModel.getReceipts()));
        }
    }

}