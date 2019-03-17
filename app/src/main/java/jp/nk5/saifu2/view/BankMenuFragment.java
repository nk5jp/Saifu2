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
import jp.nk5.saifu2.view.viewmodel.BankMenu;


public class BankMenuFragment extends Fragment implements ListView.OnItemClickListener {

    private EventListener listener;
    private ListView listView;

    public static String getTagName () {
        return "TAG_BANK_MENU";
    }

    public interface EventListener {
        void onClickMenuItem(long id);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        final View layout = inflater.inflate(R.layout.fragment_menu, container, false);

        listView = layout.findViewById(R.id.listView1);
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
        //Budget budget = ((PlanActualDTO) parent.getItemAtPosition(position)).getBudget();
        //int selectedId = budget.getId();
        //String name = budget.getName();
        //int amount = budget.getAmount();
        //boolean isValid = budget.isValid();
        //controller.clearBudget(selectedId, name, amount, isValid);
        listener.onClickMenuItem(id);
    }

}
