package jp.nk5.saifu2.view.viewmodel.menu;

public enum ShopMenu implements Menu {
    CALENDAR("CALENDAR"), STATISTICS("STATISTICS"), TAX("TAX");

    private String name;

    ShopMenu(String name) {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

}
