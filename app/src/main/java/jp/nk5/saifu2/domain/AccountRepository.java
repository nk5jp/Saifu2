package jp.nk5.saifu2.domain;

import java.util.List;

public interface AccountRepository {

    void setAccount(String name) throws Exception;
    void updateAccount(int id) throws Exception;
    Account getAccount(int id) throws Exception;
    List<Account> getAllAccount() throws Exception;
    List<Account> getAllValidAccount() throws Exception;
}
