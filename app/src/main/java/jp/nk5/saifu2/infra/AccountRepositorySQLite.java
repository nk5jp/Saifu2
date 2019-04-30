package jp.nk5.saifu2.infra;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.nk5.saifu2.domain.AccountRepository;
import jp.nk5.saifu2.domain.SpecificId;
import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Transfer;


public class AccountRepositorySQLite implements AccountRepository {

    private static AccountRepositorySQLite instance;
    private AccountDAO accountDao;
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
        accountDao = new AccountDAO(context);
        transferDAO = new TransferDAO(context, this);
        accounts = accountDao.readAll();
        transfers = transferDAO.readAll();
    }

    @Override
    public void setAccount(String name) throws Exception {
        Account account = new Account(SpecificId.NotPersisted.getId(), name, 0, true);
        accountDao.createAccount(account);
        accounts.add(account);
    }

    @Override
    public void openCloseAccount(int id) throws Exception {
        Account account = getAccount(id);
        account.setOpened(!account.isOpened());
        accountDao.updateAccount(account);
    }

    public void depositMoney(int id, int value) throws Exception {

        Account account = getAccount(id);
        account.setBalance(account.getBalance() + value);
        accountDao.updateAccount(account);

        Calendar calendar = Calendar.getInstance();

        Transfer transfer = new Transfer.Builder(
                SpecificId.NotPersisted.getId(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE),
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
        accountDao.updateAccount(debit);
        accountDao.updateAccount(credit);

        Calendar calendar = Calendar.getInstance();

        Transfer transfer = new Transfer.Builder(
                SpecificId.NotPersisted.getId(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE),
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
    public List<Transfer> getAllTransfer() { return transfers; }

    @Override
    public List<Account> getAllValidAccount()
    {
        return accounts.stream()
                .filter(Account::isOpened)
                .collect(Collectors.toList());
    }

}
