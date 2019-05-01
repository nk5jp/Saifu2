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
    private AccountRepository accountRepository;

    public TransferService(Context context, TransferFragment updateFormViewListener, AccountFragment updateInfoViewListener, BankActivity errorListener) throws Exception
    {
        accountRepository = AccountRepositorySQLite.getInstance(context);
        this.updateFormViewListener = updateFormViewListener;
        this.updateInfoViewListener = updateInfoViewListener;
        this.errorListener = errorListener;
    }

    public void setTransferAccount(int id)
    {
        try {
            Account targetAccount = accountRepository.getAccount(id);
            if (!targetAccount.isOpened()) return;

            TransferViewModel viewModel = updateFormViewListener.getViewModel();

            if (viewModel.getDebit() == null) viewModel.setDebit(targetAccount);
            else if (viewModel.getCredit() == null) viewModel.setCredit(targetAccount);

            updateFormViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    public void resetTransferAccount()
    {
        TransferViewModel viewModel = updateFormViewListener.getViewModel();

        viewModel.setCredit(null);
        viewModel.setDebit(null);
        updateFormViewListener.updateView();
    }

    public void transferMoney(int value)
    {
        try {
            TransferViewModel formViewModel = updateFormViewListener.getViewModel();

            if (isDepositCase(formViewModel)) {
                int debitId = formViewModel.getDebit().getId();
                accountRepository.depositMoney(debitId, value);
            }

            if (isTransferCase(formViewModel)) {
                int debitId = formViewModel.getDebit().getId();
                int creditId = formViewModel.getCredit().getId();
                accountRepository.transferMoney(debitId, creditId, value);
            }

            formViewModel.setCredit(null);
            formViewModel.setDebit(null);

            AccountViewModel infoViewModel = updateInfoViewListener.getViewModel();
            infoViewModel.setAccounts(accountRepository.getAllAccount());

            updateFormViewListener.updateView();
            updateInfoViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    private boolean isDepositCase(TransferViewModel viewModel)
    {
        return viewModel.getDebit() != null && viewModel.getCredit() == null;
    }

    private boolean isTransferCase(TransferViewModel viewModel)
    {
        return viewModel.getDebit() != null && viewModel.getCredit() != null;
    }

}
