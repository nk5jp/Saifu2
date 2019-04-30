package jp.nk5.saifu2.view.viewmodel;

import java.util.List;

import jp.nk5.saifu2.domain.Transfer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class TransferHistoryViewModel {

    @Getter @Setter
    private List<Transfer> transfers;

}
