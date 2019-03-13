package jp.nk5.saifu2.view.viewmodel;

public enum BankMenu implements Menu {
    ACCOUNT("ACCOUNT"), TRANSFER("TRANSFER"), TRANSFER_HISTORY("TRANSFER HISTORY");

    private String name;

    BankMenu(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
