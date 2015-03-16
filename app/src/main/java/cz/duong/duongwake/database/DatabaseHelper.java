package cz.duong.duongwake.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import cz.duong.duongwake.providers.Alarm;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alarms.db";

    private static final String SQL_CREATE_ALARMS = "CREATE TABLE " + Alarm.Entry.TABLE_NAME + "("+TextUtils.join(", ", Alarm.Entry.COLUMN_DB)+")";
    private static final String SQL_DELETE_ALARMS = "DROP TABLE IF EXISTS " + Alarm.Entry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ALARMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        sqLiteDatabase.execSQL(SQL_DELETE_ALARMS); //pro vývojářské účely...
        onCreate(sqLiteDatabase);
    }
}
