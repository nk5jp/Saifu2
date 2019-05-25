package jp.nk5.saifu2.domain;

import jp.nk5.saifu2.domain.util.MyDate;
import lombok.Getter;
import lombok.Setter;

public class Transfer {

    @Getter @Setter
    private int id;
    @Getter
    private MyDate myDate;
    @Getter
    private Account debit;
    @Getter
    private Account credit;
    @Getter
    private int value;
    @Getter
    private TransferType transferType;

    public static class Builder {
        private int id;
        private MyDate myDate;
        private Account debit;
        private Account credit = null;
        private int value;

        public Builder (int id, MyDate myDate, Account debit, int value)
        {
            this.id = id;
            this.myDate = myDate;
            this.debit = debit;
            this.value = value;
        }

        public Builder credit(Account credit)
        {
            this.credit = credit;
            return this;
        }

        public Transfer build() {
            return new Transfer(this);
        }
    }

    private Transfer(Builder builder) {
        this.id = builder.id;
        this.myDate = builder.myDate;
        this.debit = builder.debit;
        this.credit = builder.credit;
        this.value = builder.value;
        if (credit == null) this.transferType = TransferType.Deposit;
        else this.transferType = TransferType.Transfer;
    }


}
