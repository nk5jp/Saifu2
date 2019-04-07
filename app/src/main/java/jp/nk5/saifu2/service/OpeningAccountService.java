package jp.nk5.saifu2.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu2.BankActivity;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.infra.AccountRepositorySQLite;
import jp.nk5.saifu2.view.viewmodel.AccountViewModel;

public class OpeningAccountService {

    private static OpeningAccountService instance;
    private BankActivity listener;
    private AccountRepository repository;
    private AccountViewModel viewModel;

    public OpeningAccountService(Context context, BankActivity listener, AccountViewModel viewModel) throws Exception
    {
        this.repository = AccountRepositorySQLite.getInstance(context);
        this.listener = listener;
        this.viewModel = viewModel;
    }

    public void addAccount(String name)
    {
        try
        {
            repository.setAccount(name);
            viewModel.setAccounts(repository.getAllAccount());
            listener.updateView();
        } catch (Exception e) {
            listener.showError(e.getMessage());
        }
    }

    public void getAllAccount()
    {
        try
        {
            viewModel.setAccounts(repository.getAllAccount());
            listener.updateView();
        } catch (Exception e) {
            listener.showError(e.getMessage());
        }
    }
}
