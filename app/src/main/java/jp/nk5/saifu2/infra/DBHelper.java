package jp.nk5.saifu2.infra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance = null;
    private static final String DB_NAME = "nk5_saifu2.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_ACCOUNT_TABLE = "create table account ( " +
            "id integer primary key autoincrement, " +
            "name text not null, " +
            "balance integer not null, " +
            "isOpened integer not null);";

    private static final String CREATE_TRANSFER_TABLE = "create table transfer ( " +
            "id integer primary key autoincrement, " +
            "date integer not null, " +
            "debitId integer not null, " +
            "debitValue integer not null, " +
            "creditId integer not null, " +
            "creditValue integer not null);";

    private static final String DROP_ACCOUNT_TABLE = "drop table account;";
    private static final String DROP_TRANSFER_TABLE = "drop table transfer;";

    static DBHelper getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ACCOUNT_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSFER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_ACCOUNT_TABLE);
        sqLiteDatabase.execSQL(DROP_TRANSFER_TABLE);
        sqLiteDatabase.execSQL(CREATE_ACCOUNT_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSFER_TABLE);
    }

}
