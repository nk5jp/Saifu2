package jp.nk5.saifu2.infra;

import android.content.Context;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.domain.SpecificId;
import jp.nk5.saifu2.domain.Account;


public class AccountRepositorySQLite implements AccountRepository {

    private static AccountRepositorySQLite instance;
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
        Optional<Account> optional = accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
        if (optional.isPresent()) {
            Account account = optional.get();
            account.setOpened(isOpened);
            dao.updateAccount(account);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Account getAccount(int id) throws Exception
    {
        Optional<Account> optional = accounts.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
        if (optional.isPresent())
        {
            return optional.get();
        } else {
            throw new NoSuchElementException();
        }
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
                .filter(Account::isOpened)
                .collect(Collectors.toList());
    }

}
