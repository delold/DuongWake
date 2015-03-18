package cz.duong.duongwake.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import cz.duong.duongwake.database.tasks.FindAlarmsTask;
import cz.duong.duongwake.database.tasks.RemoveAlarmTask;
import cz.duong.duongwake.database.tasks.SetAlarmsTask;
import cz.duong.duongwake.listeners.AlarmGetListener;
import cz.duong.duongwake.listeners.AlarmPutListener;
import cz.duong.duongwake.listeners.AlarmRemoveListener;
import cz.duong.duongwake.providers.Alarm;

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

    public AsyncTask<?, Void, ?> getAlarms(AlarmGetListener listener) {
        return findAlarms(null, listener);
    }

    @SuppressWarnings(value = "unchecked")
    public AsyncTask<?, Void, ?> setAlarms(ArrayList<Alarm> list, AlarmPutListener listener) {
        return new SetAlarmsTask(mHelper, listener).execute(list);
    }

    public AsyncTask<?, Void, ?> findAlarms(String query, AlarmGetListener listener) {
        return new FindAlarmsTask(mHelper, listener).execute(query);
    }

    public AsyncTask<?, Void, ?> removeAlarm(Alarm a, AlarmRemoveListener listener) {
        return new RemoveAlarmTask(mHelper, listener).execute(a);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
    }
}
