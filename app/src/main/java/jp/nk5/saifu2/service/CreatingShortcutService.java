package jp.nk5.saifu2.service;

import android.content.Context;

import java.util.List;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Cost;
import jp.nk5.saifu2.domain.Shortcut;
import jp.nk5.saifu2.domain.repository.AccountRepository;
import jp.nk5.saifu2.domain.repository.CostRepository;
import jp.nk5.saifu2.domain.repository.ShortcutRepository;
import jp.nk5.saifu2.infra.repository.AccountRepositorySQLite;
import jp.nk5.saifu2.infra.repository.CostRepositorySQLite;
import jp.nk5.saifu2.infra.repository.ShortcutRepositorySQLite;

public class CreatingShortcutService {

    private AccountRepository accountRepository;
    private CostRepository costRepository;
    private ShortcutRepository shortcutRepository;

    public CreatingShortcutService(Context context) throws Exception
    {
        this.accountRepository = AccountRepositorySQLite.getInstance(context);
        this.costRepository = CostRepositorySQLite.getInstance(context);
        this.shortcutRepository = ShortcutRepositorySQLite.getInstance(context);
    }

    public List<Account> getValidAccount() throws Exception
    {
        return accountRepository.getAllValidAccount();
    }

    public List<Cost> getValidCost() throws Exception
    {
        return costRepository.getValidCost();
    }

    public Shortcut getShortcut(int boxId) throws Exception
    {
        return shortcutRepository.getShortcutByBoxId(boxId);
    }

    public void createShortcut(String name, int boxId, int typeId, int firstId, int secondId, int value) throws Exception
    {
        shortcutRepository.setShortcut(name, boxId, typeId, firstId, secondId, value);
    }

    public void deleteShortcut(int boxId) throws Exception
    {
        shortcutRepository.deleteShortcutByBoxId(boxId);
    }

}
