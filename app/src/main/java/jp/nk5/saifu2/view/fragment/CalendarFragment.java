package jp.nk5.saifu2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.ReceiptListAdapter;
import jp.nk5.saifu2.view.viewmodel.CalendarViewModel;

public class CalendarFragment extends Fragment implements ListView.OnItemClickListener, CalendarView.OnDateChangeListener {

    private EventListener listener;
    private CalendarViewModel viewModel = new CalendarViewModel(null, new ArrayList<>());
    private View layout;

    public interface EventListener {
        void onItemClick(int position);
        void onSelectedDayChange(int year, int month, int day);
    }

    public static String getTagName()
    {
        return "TAG_CALENDAR";
    }

    /**
     * 本日日付で画面モデルを初期化し，画面表示を更新する
     * @param inflater 継承元の引数
     * @param container 継承元の引数
     * @param savedInstanceState 継承元の引数
     * @return 作成されたビュー
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_calendar, container, false);
        updateView();

        ListView listView = layout.findViewById(R.id.listView1);
        CalendarView calendarView = layout.findViewById(R.id.calendarView1);
        Context context = this.getContext();
        if (context != null)
        {
            listView.setOnItemClickListener(this);
            calendarView.setOnDateChangeListener(this);
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

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
        listener.onSelectedDayChange(year, month + 1, day);
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