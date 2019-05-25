package jp.nk5.saifu2.view.viewmodel.menu;

public enum TopMenu implements Menu {
    SHOP("SHOP"), BANK("BANK"), ACCOUNT_BOOK("ACCOUNT_BOOK");

    private String name;

    TopMenu(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

}
