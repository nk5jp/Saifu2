package jp.nk5.saifu2.domain;

public enum SpecificId {
    NotPersisted(-1), MeansNull(-2);

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
