package cz.duong.duongwake.database.tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cz.duong.duongwake.listeners.AlarmGetListener;
import cz.duong.duongwake.providers.Alarm;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class FindAlarmsTask extends DatabaseTask<String, ArrayList<Alarm>, AlarmGetListener> {
    public FindAlarmsTask(SQLiteOpenHelper sqLiteOpenHelper, AlarmGetListener listener) {
        super(sqLiteOpenHelper, listener);
    }

    @Override
    protected ArrayList<Alarm> doInBackground(String... params) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(Alarm.Entry.TABLE_NAME, Alarm.Entry.SELECTOR_SQL, params[0], null, null, null, null);

        ArrayList<Alarm> res = new ArrayList<>();
        while(c.moveToNext()) {
            long id = c.getLong(c.getColumnIndexOrThrow(Alarm.Entry._ID));
            String name = c.getString(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_NAME));
            int hour = c.getInt(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_HOUR));
            int minute = c.getInt(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_MINUTE));
            int enabled = c.getInt(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_ENABLED));
            int repeated = c.getInt(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_REPEATED));
            String days = c.getString(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_DAYS));
            Long time = c.getLong(c.getColumnIndexOrThrow(Alarm.Entry.COLUMN_ALARM_TIME));

            Alarm a = new Alarm(name, hour, minute, days, enabled, repeated, time);
            a.setId(id);

            res.add(a);
        }

        c.close();
        db.close();

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
