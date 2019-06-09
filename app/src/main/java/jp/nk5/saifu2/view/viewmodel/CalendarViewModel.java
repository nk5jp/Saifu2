package jp.nk5.saifu2.view.viewmodel;

import java.util.List;

import jp.nk5.saifu2.domain.Receipt;
import jp.nk5.saifu2.domain.util.MyDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class CalendarViewModel {

    @Getter @Setter
    private MyDate date;
    @Getter @Setter
    private List<Receipt> receipts;

}
