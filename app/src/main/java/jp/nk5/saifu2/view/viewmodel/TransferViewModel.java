package jp.nk5.saifu2.view.viewmodel;

import jp.nk5.saifu2.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class TransferViewModel {

    @Getter @Setter
    private Account debit;
    @Getter @Setter
    private Account credit;

}
