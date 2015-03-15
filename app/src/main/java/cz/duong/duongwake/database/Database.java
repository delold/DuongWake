package cz.duong.duongwake.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import cz.duong.duongwake.Alarm;
import cz.duong.duongwake.database.tasks.FindAlarmsTask;
import cz.duong.duongwake.database.tasks.SetAlarmsTask;
import cz.duong.duongwake.listeners.DatabaseListeners;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class Database {
    private DatabaseHelper mHelper;

    public Database(DatabaseHelper helper) {
        mHelper = helper;
    }

    public Database(Context context) {
        this(new DatabaseHelper(context));
    }

    public AsyncTask<?, Void, ?> getAlarms(DatabaseListeners.AlarmGetListener listener) {
        return findAlarms(null, listener);
    }

    @SuppressWarnings(value = "unchecked")
    public AsyncTask<?, Void, ?> setAlarm(ArrayList<Alarm> list, DatabaseListeners.AlarmPutListener listener) {
        return new SetAlarmsTask(mHelper, listener).execute(list);
    }

    public AsyncTask<?, Void, ?> findAlarms(String query, DatabaseListeners.AlarmGetListener listener) {
        return new FindAlarmsTask(mHelper, listener).execute(query);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }
}
