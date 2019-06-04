package jp.nk5.saifu2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Cost;

public class CostListAdapter extends ArrayAdapter<Cost> {

    private LayoutInflater layoutInflater;

    public CostListAdapter(@NonNull Context context, int resource, @NonNull List<Cost> objects) {
        super(context, resource, objects);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent)
    {
        if (view == null)
        {
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        Cost cost = getItem(position);
        if (cost != null)
        {
            TextView textView = view.findViewById(android.R.id.text1);
            if (!cost.isValid()) textView.setBackgroundColor(Color.GRAY);
            if (cost.getEstimate() == 0)
            {
                textView.setText(
                        String.format(
                                Locale.JAPAN,
                                "%s(%d) : %,d/-",
                                cost.getName(),
                                cost.getDate().getFullDate(),
                                cost.getResult()
                        )
                );
            } else {
                textView.setText(
                        String.format(
                                Locale.JAPAN,
                                "%s(%d) : %,d/%,d",
                                cost.getName(),
                                cost.getDate().getFullDate(),
                                cost.getResult(),
                                cost.getEstimate()
                        )
                );
                if (cost.getEstimate() < cost.getResult()) textView.setTextColor(Color.RED);
            }
        }
        return view;
    }
}
