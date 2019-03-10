package jp.nk5.saifu2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import jp.nk5.saifu2.R;

public class MenuFragment extends Fragment implements ListView.OnItemClickListener {

    private EventListener listener;
    private List<String> menuList;

    public static String getTagName () {
        return "TAG_MENU";
    }

    public interface EventListener {
        void onClickMenuItem(long id);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_menu, container, false);

        ListView listView = layout.findViewById(R.id.listView1);
        listView.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, new String[]{"SHOP", "BANK", "ACCOUNT BOOK"}));
        listView.setOnItemClickListener(this);
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
        //Budget budget = ((PlanActualDTO) parent.getItemAtPosition(position)).getBudget();
        //int selectedId = budget.getId();
        //String name = budget.getName();
        //int amount = budget.getAmount();
        //boolean isValid = budget.isValid();
        //controller.clearBudget(selectedId, name, amount, isValid);
        listener.onClickMenuItem(id);
    }

}
