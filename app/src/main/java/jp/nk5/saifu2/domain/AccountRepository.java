package jp.nk5.saifu2.domain;

import java.util.List;

public interface AccountRepository {

    void setAccount(String name) throws Exception;
    void openCloseAccount(int id) throws Exception;
    void depositMoney(int id, int value) throws Exception;
    void transferMoney(int debitId, int creditId, int value) throws Exception;
    Account getAccount(int id) throws Exception;
    List<Account> getAllAccount() throws Exception;
    List<Transfer> getAllTransfer() throws Exception;
    List<Account> getAllValidAccount() throws Exception;
}
