package jp.nk5.saifu2.domain.util;

public enum SpecificId {
    NotPersisted(-1), MeansNull(-2),
    Shop(0), Transfer(1);

    private int id;

    SpecificId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}
