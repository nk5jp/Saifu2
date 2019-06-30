package jp.nk5.saifu2.domain;

import jp.nk5.saifu2.domain.util.MyDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Receipt {

    @Setter @Getter
    private int id;
    @Getter
    private MyDate date;
    @Getter
    private Account account;
    @Getter @Setter
    private int sum;

}
