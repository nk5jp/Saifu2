package jp.nk5.saifu2.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ReceiptDetail {

    @Getter @Setter
    private int id;
    @Getter
    private Receipt receipt;
    @Getter
    private Cost cost;
    @Getter
    private int value;

}
