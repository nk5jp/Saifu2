package jp.nk5.saifu2.view.fragment.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.adapter.MenuListAdapter;
import jp.nk5.saifu2.view.viewmodel.menu.BankMenu;


public class BankMenuFragment extends Fragment implements ListView.OnItemClickListener {

    private EventListener listener;

    public static String getTagName () {
        return "TAG_BANK_MENU";
    }

    public interface EventListener {
        void onClickMenuItem(BankMenu menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_menu, container, false);

        ListView listView = layout.findViewById(R.id.listView1);
        Context context = this.getContext();
        if (context != null)
        {
            listView.setAdapter(new MenuListAdapter(context, android.R.layout.simple_list_item_1, BankMenu.values()));
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
            throw new ClassCastException(context.toString() + " must implement MenuFragmentListener");
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BankMenu menu = (BankMenu) parent.getItemAtPosition(position);
        listener.onClickMenuItem(menu);
    }

}
