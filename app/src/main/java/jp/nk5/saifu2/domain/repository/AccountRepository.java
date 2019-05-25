package jp.nk5.saifu2.domain.repository;

import java.util.List;

import jp.nk5.saifu2.domain.Account;
import jp.nk5.saifu2.domain.Transfer;

public interface AccountRepository {

    void setAccount(String name) throws Exception;
    void openCloseAccount(int id) throws Exception;
    void depositMoney(int id, int value) throws Exception;
    void transferMoney(int debitId, int creditId, int value) throws Exception;
    Account getAccount(int id) throws Exception;
    List<Account> getAllAccount() throws Exception;
    List<Transfer> getAllTransfer() throws Exception;
    List<Transfer> getSpecificTransfer(int year, int month) throws Exception;
    List<Account> getAllValidAccount() throws Exception;
}
