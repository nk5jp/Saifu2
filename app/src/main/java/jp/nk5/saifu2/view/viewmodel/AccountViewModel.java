package jp.nk5.saifu2.view.viewmodel;

import java.util.List;

import jp.nk5.saifu2.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class AccountViewModel {

    @Getter @Setter
    private List<Account> accounts;

}
