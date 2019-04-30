package jp.nk5.saifu2.domain;

import lombok.Getter;
import lombok.Setter;

public class Transfer {

    @Getter @Setter
    private int id;
    @Getter
    private int year;
    @Getter
    private int month;
    @Getter
    private int day;
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
        private int year;
        private int month;
        private int day;
        private Account debit;
        private Account credit = null;
        private int value;

        public Builder (int id, int year, int month, int day, Account debit, int value)
        {
            this.id = id;
            this.year = year;
            this.month = month;
            this.day = day;
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
        this.year = builder.year;
        this.month = builder.month;
        this.day = builder.day;
        this.debit = builder.debit;
        this.credit = builder.credit;
        this.value = builder.value;
        if (credit == null) this.transferType = TransferType.Deposit;
        else this.transferType = TransferType.Transfer;
    }


}
