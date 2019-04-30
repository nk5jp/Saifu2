package jp.nk5.saifu2.service;

import android.content.Context;

import jp.nk5.saifu2.BankActivity;
import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.infra.AccountRepositorySQLite;
import jp.nk5.saifu2.view.AccountFragment;
import jp.nk5.saifu2.view.viewmodel.AccountViewModel;

public class OpeningAccountService {

    private BankActivity errorListener;
    private AccountFragment updateViewListener;
    private AccountRepository repository;
    private AccountViewModel viewModel;

    public OpeningAccountService(Context context, AccountFragment updateViewListener, BankActivity errorListener) throws Exception
    {
        this.repository = AccountRepositorySQLite.getInstance(context);
        this.updateViewListener = updateViewListener;
        this.errorListener = errorListener;
        this.viewModel = updateViewListener.getViewModel();
    }

    public void addAccount(String name)
    {
        try
        {
            if (isDuplicated(name)) {
                errorListener.showError("Name is duplicated.");
                return;
            }
            repository.setAccount(name);
            viewModel.setAccounts(repository.getAllAccount());
            updateViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    public void getAllAccount()
    {
        try
        {
            viewModel.setAccounts(repository.getAllAccount());
            updateViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    public void updateAccountStatus(int id)
    {
        try {
            repository.openCloseAccount(id);
            viewModel.setAccounts(repository.getAllAccount());
            updateViewListener.updateView();
        } catch (Exception e) {
            errorListener.showError(e.getMessage());
        }
    }

    private boolean isDuplicated(String name) throws Exception
    {
            return repository.getAllAccount().stream()
                    .filter(a -> a.getName().equals(name))
                    .count() != 0;
    }
}
