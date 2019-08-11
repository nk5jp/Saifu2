package jp.nk5.saifu2.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.domain.Shortcut;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.view.viewmodel.TopViewModel;


public class TopFragment extends Fragment implements TextView.OnClickListener, TextView.OnLongClickListener {

    public static String getTagName()
    {
        return "TAG_TOP";
    }

    private EventListener listener;
    private TopViewModel viewModel = new TopViewModel();
    private View layout;

    public interface EventListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }

    public TopViewModel getViewModel() {return this.viewModel; }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_top, container, false);
        updateView();

        TextView textView;
        Context context = this.getContext();
        if (context != null)
        {
            textView = layout.findViewById(R.id.textView1);
            textView.setOnClickListener(this);
            textView.setOnLongClickListener(this);
            textView = layout.findViewById(R.id.textView2);
            textView.setOnClickListener(this);
            textView.setOnLongClickListener(this);
            textView = layout.findViewById(R.id.textView3);
            textView.setOnClickListener(this);
            textView.setOnLongClickListener(this);
            textView = layout.findViewById(R.id.textView4);
            textView.setOnClickListener(this);
            textView.setOnLongClickListener(this);
            textView = layout.findViewById(R.id.textView5);
            textView.setOnClickListener(this);
            textView.setOnLongClickListener(this);
            textView = layout.findViewById(R.id.textView6);
            textView.setOnClickListener(this);
            textView.setOnLongClickListener(this);
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

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        if (id == R.id.textView1) listener.onItemClick(0);
        else if (id == R.id.textView2) listener.onItemClick(1);
        else if (id == R.id.textView3) listener.onItemClick(2);
        else if (id == R.id.textView4) listener.onItemClick(3);
        else if (id == R.id.textView5) listener.onItemClick(4);
        else if (id == R.id.textView6) listener.onItemClick(5);
    }

    @Override
    public boolean onLongClick(View view)
    {
        int id = view.getId();
        if (id == R.id.textView1) listener.onItemLongClick(0);
        else if (id == R.id.textView2) listener.onItemLongClick(1);
        else if (id == R.id.textView3) listener.onItemLongClick(2);
        else if (id == R.id.textView4) listener.onItemLongClick(3);
        else if (id == R.id.textView5) listener.onItemLongClick(4);
        else if (id == R.id.textView6) listener.onItemLongClick(5);
        return true;
    }

    public void updateView()
    {
        Context context = this.getContext();
        int[] viewIds = new int[]{R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5, R.id.textView6};
        if (context != null) {
            for (int i = 0; i < 6; i++)
            {
                TextView textView = layout.findViewById(viewIds[i]);
                Shortcut shortcut = viewModel.getShortcuts()[i];
                if (shortcut == null)
                {
                    textView.setText(R.string.str_not_registered);
                    textView.setBackgroundColor(Color.GRAY);
                } else {
                    textView.setText(shortcut.getName());
                    if (shortcut.getTypeId() == SpecificId.Shop.getId()) textView.setBackgroundColor(Color.CYAN);
                    if (shortcut.getTypeId() == SpecificId.Transfer.getId()) textView.setBackgroundColor(Color.YELLOW);
                }
            }
        }
    }
}
