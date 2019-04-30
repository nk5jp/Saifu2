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
    private int debitValue;
    @Getter
    private int creditValue;
    @Getter
    private TransferType transferType;

    public static class Builder {
        private int id;
        private int year;
        private int month;
        private int day;
        private Account debit;
        private Account credit = null;
        private int debitValue;
        private int creditValue = 0;

        public Builder (int id, int year, int month, int day, Account debit, int debitValue)
        {
            this.id = id;
            this.year = year;
            this.month = month;
            this.day = day;
            this.debit = debit;
            this.debitValue = debitValue;
        }

        public Builder credit(Account credit, int creditValue)
        {
            this.credit = credit;
            this.creditValue = creditValue;
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
        this.debitValue = builder.debitValue;
        this.creditValue = builder.creditValue;
        if (credit == null) this.transferType = TransferType.Deposit;
        else this.transferType = TransferType.Transfer;
    }


}
