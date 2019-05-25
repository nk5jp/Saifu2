package jp.nk5.saifu2.view.viewmodel.menu;

public enum AccountBookMenu implements Menu {
    COST("COST"), TEMPLATE("TEMPLATE"), EXTRA("EXTRA");

    private String name;

    AccountBookMenu(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

}
