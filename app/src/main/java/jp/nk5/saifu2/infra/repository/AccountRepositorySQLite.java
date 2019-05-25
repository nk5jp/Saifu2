package jp.nk5.saifu2.infra.repository;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.domain.util.MyDate;
import jp.nk5.saifu2.domain.util.SpecificId;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Transfer;
import jp.nk5.saifu2.infra.dao.AccountDAO;
import jp.nk5.saifu2.infra.dao.TransferDAO;


public class AccountRepositorySQLite implements AccountRepository {

    private static AccountRepositorySQLite instance;
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;
    private List<Account> accounts;
    private List<Transfer> transfers;

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
        accountDAO = new AccountDAO(context);
        transferDAO = new TransferDAO(context, this);
        accounts = accountDAO.readAll();
        transfers = transferDAO.readAll();
    }

    @Override
    public void setAccount(String name) throws Exception {
        Account account = new Account(SpecificId.NotPersisted.getId(), name, 0, true);
        accountDAO.createAccount(account);
        accounts.add(account);
    }

    @Override
    public void openCloseAccount(int id) throws Exception {
        Account account = getAccount(id);
        account.setOpened(!account.isOpened());
        accountDAO.updateAccount(account);
    }

    public void depositMoney(int id, int value) throws Exception {

        Account account = getAccount(id);
        account.setBalance(account.getBalance() + value);
        accountDAO.updateAccount(account);

        Calendar calendar = Calendar.getInstance();
        MyDate today = new MyDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE)
        );

        Transfer transfer = new Transfer.Builder(
                SpecificId.NotPersisted.getId(),
                today,
                account,
                value
        ).build();
        transferDAO.createTransfer(transfer);
        transfers.add(transfer);
    }

    public void transferMoney(int debitId, int creditId, int value) throws Exception {

        Account debit = getAccount(debitId);
        Account credit = getAccount(creditId);
        debit.setBalance(debit.getBalance() + value);
        credit.setBalance(credit.getBalance() - value);
        accountDAO.updateAccount(debit);
        accountDAO.updateAccount(credit);

        Calendar calendar = Calendar.getInstance();
        MyDate today = new MyDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE)
        );

        Transfer transfer = new Transfer.Builder(
                SpecificId.NotPersisted.getId(),
                today,
                debit,
                value
        ).credit(
                credit
        )
        .build();
        transferDAO.createTransfer(transfer);
        transfers.add(transfer);
    }

    @Override
    public Account getAccount(int id)
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
    public List<Transfer> getAllTransfer() { return transfers; }

    @Override
    public List<Transfer> getSpecificTransfer(int year, int month)
    {
        return transfers.stream()
                .filter(t -> t.getMyDate().getYear() == year && t.getMyDate().getMonth() == month)
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> getAllValidAccount()
    {
        return accounts.stream()
                .filter(Account::isOpened)
                .collect(Collectors.toList());
    }

}
