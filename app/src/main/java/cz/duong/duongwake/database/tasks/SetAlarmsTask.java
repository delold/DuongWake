package cz.duong.duongwake.database.tasks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cz.duong.duongwake.Alarm;
import cz.duong.duongwake.listeners.DatabaseListeners;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class SetAlarmsTask extends DatabaseTask<ArrayList<Alarm>, Boolean, DatabaseListeners.AlarmPutListener> {

    public SetAlarmsTask(SQLiteOpenHelper sqLiteOpenHelper, DatabaseListeners.AlarmPutListener listener) {
        super(sqLiteOpenHelper, listener);
    }

    @Override
    protected Boolean doInBackground(ArrayList<Alarm>... params) {

        ArrayList<Alarm> alarms = params[0];

        SQLiteDatabase db = getWritableDatabase();

        for(Alarm alarm : alarms) {
            ContentValues values = new ContentValues();
            values.put(Alarm.Entry.COLUMN_ALARM_NAME, alarm.getName());
            values.put(Alarm.Entry.COLUMN_ALARM_HOUR, alarm.getHour());
            values.put(Alarm.Entry.COLUMN_ALARM_MINUTE, alarm.getMinute());
            values.put(Alarm.Entry.COLUMN_ALARM_ENABLED, alarm.getEnabled());
            values.put(Alarm.Entry.COLUMN_ALARM_REPEATED, alarm.getRepeated());
            values.put(Alarm.Entry.COLUMN_ALARM_DAYS, alarm.getDaysString());

            db.insertOrThrow(Alarm.Entry.TABLE_NAME, null, values);
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(getListener() != null) {
            getListener().onAlarmPut();
        }

        super.onPostExecute(result);
    }
}
