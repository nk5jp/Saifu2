package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.BankActivity;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.infra.AccountRepositorySQLite;
import jp.nk5.saifu2.view.AccountFragment;
import jp.nk5.saifu2.view.TransferFragment;
import jp.nk5.saifu2.view.viewmodel.TransferViewModel;

public class TransferService {

    private BankActivity errorListener;
    private TransferFragment updateFormViewListener;
    private TransferViewModel formViewModel;
    private AccountRepository accountRepository;

    public TransferService(Context context, TransferFragment updateFormViewListener, BankActivity errorListener) throws Exception
    {
        accountRepository = AccountRepositorySQLite.getInstance(context);
        this.updateFormViewListener = updateFormViewListener;
        this.formViewModel = updateFormViewListener.getViewModel();
        this.errorListener = errorListener;
    }

    public void setTransferAccount(int id)
    {
        try {
            Account targetAccount = accountRepository.getAccount(id);
            if (!targetAccount.isOpened()) return;

            if (formViewModel.getAccountOfTo() == null) formViewModel.setAccountOfTo(targetAccount);
            else if (formViewModel.getAccountOfFrom() == null) formViewModel.setAccountOfFrom(targetAccount);

            updateFormViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }


}
