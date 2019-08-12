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

import jp.nk5.saifu2.domain.Transfer;

public class TransferListAdapter extends ArrayAdapter<Transfer> {

    private LayoutInflater layoutInflater;

    public TransferListAdapter(@NonNull Context context, int resource, @NonNull List<Transfer> objects) {
        super(context, resource, objects);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        Transfer transfer = getItem(position);
        if (transfer != null) {
            TextView textView = view.findViewById(android.R.id.text1);
            switch (transfer.getTransferType()) {
                case Deposit:
                    textView.setText(
                            String.format(
                                    Locale.JAPAN,
                                    "%s:%sに%,d円を振込",
                                    transfer.getDate().getFullDateWithFormat(),
                                    transfer.getDebit().getName(),
                                    transfer.getValue()
                            )
                    );
                    break;
                case Transfer:
                    textView.setText(
                            String.format(
                                    Locale.JAPAN,
                                    "%s:%sから%sに%,d円を振替",
                                    transfer.getDate().getFullDateWithFormat(),
                                    transfer.getCredit().getName(),
                                    transfer.getDebit().getName(),
                                    transfer.getValue()
                            )
                    );
                    break;
            }
        }
        return view;
    }
}