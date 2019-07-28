package jp.nk5.saifu2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import jp.nk5.saifu2.view.viewmodel.ReceiptDetailViewModel;

public class ReceiptDetailListAdapter extends ArrayAdapter<ReceiptDetailViewModel.ReceiptDetailForView> {

    private LayoutInflater layoutInflater;

    public ReceiptDetailListAdapter(@NonNull Context context, int resource, @NonNull List<ReceiptDetailViewModel.ReceiptDetailForView> objects) {
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

        ReceiptDetailViewModel.ReceiptDetailForView detail = getItem(position);
        if (detail != null)
        {
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(
                    String.format(Locale.JAPAN, "%s：%,d円", detail.getCost().getName(), detail.getValue())
            );
        }
        return view;
    }
}
