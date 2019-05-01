package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.BankActivity;
import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.infra.AccountRepositorySQLite;
import jp.nk5.saifu2.view.TransferHistoryFragment;
import jp.nk5.saifu2.view.viewmodel.TransferHistoryViewModel;

public class SearchingTransferService {

    private BankActivity errorListener;
    private TransferHistoryFragment updateViewListener;
    private AccountRepository repository;
    private TransferHistoryViewModel viewModel;

    public SearchingTransferService(Context context, TransferHistoryFragment updateViewListener, BankActivity errorListener) throws Exception
    {
        this.repository = AccountRepositorySQLite.getInstance(context);
        this.updateViewListener = updateViewListener;
        this.errorListener = errorListener;
        this.viewModel = updateViewListener.getViewModel();
    }

    public void getAllTransfer()
    {
        try
        {
            viewModel.setTransfers(repository.getAllTransfer());
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    public void getSpecificTransfer(int date)
    {
        try {
            if (!isValid(date)) {
                errorListener.showError("this is obviously invalid date");
                return;
            }

            int year = date / 100;
            int month = date - year * 100;
            viewModel.setTransfers(repository.getSpecificTransfer(year, month));
            updateViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    private boolean isValid(int date)
    {
        return String.valueOf(date).length() == 6;
    }

}
