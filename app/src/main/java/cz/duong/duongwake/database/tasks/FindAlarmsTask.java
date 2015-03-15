package cz.duong.duongwake.database.tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cz.duong.duongwake.Alarm;
import cz.duong.duongwake.listeners.DatabaseListeners;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class FindAlarmsTask extends DatabaseTask<String, ArrayList<Alarm>, DatabaseListeners.AlarmGetListener> {
    public FindAlarmsTask(SQLiteOpenHelper sqLiteOpenHelper, DatabaseListeners.AlarmGetListener listener) {
        super(sqLiteOpenHelper, listener);
    }

    @Override
    protected ArrayList<Alarm> doInBackground(String... params) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(Alarm.Entry.TABLE_NAME, Alarm.Entry.SELECTOR_SQL, params[0], null, null, null, null);



        ArrayList<Alarm> res = new ArrayList<>();
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_NAME));
            int hour = c.getInt(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_HOUR));
            int minute = c.getInt(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_MINUTE));
            int enabled = c.getInt(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_ENABLED));
            int repeated = c.getInt(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_REPEATED));
            String days = c.getString(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_DAYS));

            res.add(new Alarm(name, hour, minute, days, enabled, repeated));
        }

        c.close();

        return res;
    }

    @Override
    protected void onPostExecute(ArrayList<Alarm> result) {
        if(getListener() != null) {
            getListener().onAlarmGet(result);
        }
        super.onPostExecute(result);
    }
}
