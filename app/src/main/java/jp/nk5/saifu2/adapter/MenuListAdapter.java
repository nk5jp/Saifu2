package jp.nk5.saifu2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import jp.nk5.saifu2.view.viewmodel.Menu;

public class MenuListAdapter extends ArrayAdapter<Menu> {

    private LayoutInflater layoutInflater;

    public MenuListAdapter(@NonNull Context context, int resource, @NonNull Menu[] objects) {
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

        Menu menu = getItem(position);
        if (menu != null)
        {
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(menu.getName());
        }
        return view;
    }





}
