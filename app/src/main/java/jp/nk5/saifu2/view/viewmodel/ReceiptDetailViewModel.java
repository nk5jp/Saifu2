package jp.nk5.saifu2.view.viewmodel;

import jp.nk5.saifu2.domain.Cost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ReceiptDetailViewModel {


    @AllArgsConstructor
    public class ReceiptDetailForView
    {
        @Getter @Setter
        private Cost cost;
        @Getter @Setter
        private int value;
    }
}
