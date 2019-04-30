package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.BankActivity;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.infra.AccountRepositorySQLite;
import jp.nk5.saifu2.view.AccountFragment;
import jp.nk5.saifu2.view.TransferFragment;
import jp.nk5.saifu2.view.viewmodel.AccountViewModel;
import jp.nk5.saifu2.view.viewmodel.TransferViewModel;

public class TransferService {

    private BankActivity errorListener;
    private TransferFragment updateFormViewListener;
    private AccountFragment updateInfoViewListener;
    private TransferViewModel formViewModel;
    private AccountViewModel infoViewModel;
    private AccountRepository accountRepository;

    public TransferService(Context context, TransferFragment updateFormViewListener, AccountFragment updateInfoViewListener, BankActivity errorListener) throws Exception
    {
        accountRepository = AccountRepositorySQLite.getInstance(context);
        this.updateFormViewListener = updateFormViewListener;
        this.updateInfoViewListener = updateInfoViewListener;
        this.formViewModel = updateFormViewListener.getViewModel();
        this.infoViewModel = updateInfoViewListener.getViewModel();
        this.errorListener = errorListener;
    }

    public void setTransferAccount(int id)
    {
        try {
            Account targetAccount = accountRepository.getAccount(id);
            if (!targetAccount.isOpened()) return;

            if (formViewModel.getDebit() == null) formViewModel.setDebit(targetAccount);
            else if (formViewModel.getCredit() == null) formViewModel.setCredit(targetAccount);

            updateFormViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    public void resetTransferAccount()
    {
        formViewModel.setCredit(null);
        formViewModel.setDebit(null);
        updateFormViewListener.updateView();
    }

    public void transferMoney(int value)
    {
        try {
            if (formViewModel.getDebit() != null && formViewModel.getCredit() == null) {
                int debitId = formViewModel.getDebit().getId();
                accountRepository.depositMoney(debitId, value);
            }

            if (formViewModel.getDebit() != null && formViewModel.getCredit() != null) {
                int debitId = formViewModel.getDebit().getId();
                int creditId = formViewModel.getCredit().getId();
                accountRepository.transferMoney(debitId, creditId, value);
            }

            formViewModel.setCredit(null);
            formViewModel.setDebit(null);
            infoViewModel.setAccounts(accountRepository.getAllAccount());
            updateFormViewListener.updateView();
            updateInfoViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

}
