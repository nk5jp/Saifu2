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

import jp.nk5.saifu2.view.viewmodel.TemplateViewModel;

public class TemplateListAdapter extends ArrayAdapter<TemplateViewModel.TemplateForView> {

    private LayoutInflater layoutInflater;

    public TemplateListAdapter(@NonNull Context context, int resource, @NonNull List<TemplateViewModel.TemplateForView> objects) {
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

        TemplateViewModel.TemplateForView templateForView = getItem(position);
        if (templateForView != null)
        {
            TextView textView = view.findViewById(android.R.id.text1);

            if (templateForView.getTemplate().isControlled()) textView.setText(templateForView.getTemplate().getName());
            else textView.setText(String.format("%s (UnControlled)", templateForView.getTemplate().getName()));

            if (!templateForView.getTemplate().isValid()) textView.setBackgroundColor(Color.GRAY);
            else if (templateForView.isSelected()) textView.setBackgroundColor(Color.YELLOW);
        }
        return view;
    }
}
