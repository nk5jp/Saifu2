package jp.nk5.saifu2.infra;

public class SQLiteBoolean {

    public static boolean getBoolean(int i)
    {
        return i == 1;
    }

    public static int getInt(boolean bool)
    {
        if (bool)
        {
            return 1;
        } else {
            return 0;
        }

    }

}
