package jp.nk5.saifu2.domain;

import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

public class Account {

    @Getter
    private int id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int balance;

    public Account (int id, String name, int balance) throws Exception
    {
        if (name.equals("")) throw new Exception();

        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString()
    {
        return String.format(Locale.JAPAN, "%s:%,d", name, balance);
    }


}
