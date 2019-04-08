package jp.nk5.saifu2.view;

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
import jp.nk5.saifu2.view.viewmodel.TopMenu;

public class TopMenuFragment extends Fragment implements ListView.OnItemClickListener {

    private EventListener listener;

    public static String getTagName () {
        return "TAG_MENU";
    }

    public interface EventListener {
        void onClickMenuItem(TopMenu menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_menu, container, false);

        ListView listView = layout.findViewById(R.id.listView1);
        Context context = this.getContext();
        if (context != null)
        {
            listView.setAdapter(new MenuListAdapter(context, android.R.layout.simple_list_item_1, TopMenu.values()));
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
        TopMenu menu = (TopMenu) parent.getItemAtPosition(position);
        listener.onClickMenuItem(menu);
    }

}
