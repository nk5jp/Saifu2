package jp.nk5.saifu2.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.nk5.saifu2.R;
import jp.nk5.saifu2.view.viewmodel.TransferViewModel;

public class TransferFragment extends Fragment {

    private TransferViewModel viewModel = new TransferViewModel(null, null);
    private View layout;

    public static String getTagName()
    {
        return "TAG_TRANSFER";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container  , Bundle savedInstanceState)
    {
        layout = inflater.inflate(R.layout.fragment_transfer, container, false);
        return layout;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        viewModel.setDebit(null);
        viewModel.setCredit(null);
    }

    public TransferViewModel getViewModel()
    {
        return this.viewModel;
    }

    public void updateView()
    {
        Context context = this.getContext();
        if (context != null) {
            TextView textView = layout.findViewById(R.id.textView1);
            if (viewModel.getDebit() == null) textView.setText(android.R.string.unknownName);
            else textView.setText(viewModel.getDebit().getName());

            textView = layout.findViewById(R.id.textView2);
            if (viewModel.getCredit() == null) textView.setText(android.R.string.unknownName);
            else textView.setText(viewModel.getCredit().getName());
        }
    }


}
