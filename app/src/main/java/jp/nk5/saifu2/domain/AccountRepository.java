package jp.nk5.saifu2.domain;

import java.util.List;

public interface AccountRepository {

    void setAccount(int id, String name);
    Account getAccount(int id);
    List<Account> getAllAccount();
    List<Account> getAllValidAccount();
}
