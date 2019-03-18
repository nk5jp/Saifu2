package jp.nk5.saifu2.infra;

import android.content.Context;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.domain.SpecificId;
import jp.nk5.saifu2.domain.Account;


public class AccountRepositorySQLite implements AccountRepository {

    private static AccountRepositorySQLite instance;
    private Context context;
    private AccountDAO dao;
    private List<Account> accounts;

    public static AccountRepositorySQLite getInstance(Context context) throws Exception
    {
        if (instance == null)
        {
            instance = new AccountRepositorySQLite(context);
        }
        return instance;
    }

    private AccountRepositorySQLite (Context context) throws Exception
    {
        this.context = context;
        dao = new AccountDAO(context);
        accounts = dao.readAll();
    }


    @Override
    public void setAccount(String name) throws Exception {
        Account account = new Account(SpecificId.NotPersisted.getId(), name, 0, true);
        dao.createAccount(account);
        accounts.add(account);
    }

    @Override
    public void updateAccount(int id,  boolean isOpened) throws Exception {
        Account account = accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .get();
        account.setOpened(isOpened);
        dao.updateAccount(account);
    }

    @Override
    public Account getAccount(int id) throws Exception
    {
        return accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<Account> getAllAccount()
    {
        return accounts;
    }

    @Override
    public List<Account> getAllValidAccount()
    {
        return accounts.stream()
                .filter(a -> a.isOpened())
                .collect(Collectors.toList());
    }

}
