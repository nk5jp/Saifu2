package jp.nk5.saifu2.domain;

import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

public class Account {

    @Getter @Setter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int balance;
    @Getter @Setter
    private boolean isOpened;

    public Account (int id, String name, int balance, boolean isOpened) throws Exception
    {
        if (name.equals("")) throw new Exception();

        this.id = id;
        this.name = name;
        this.balance = balance;
        this.isOpened = isOpened;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.JAPAN, "%s:%,d", name, balance);
    }


}
