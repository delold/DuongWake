package cz.duong.duongwake.database.tasks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import cz.duong.duongwake.listeners.AlarmPutListener;
import cz.duong.duongwake.providers.Alarm;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class SetAlarmsTask extends DatabaseTask<ArrayList<Alarm>, ArrayList<Alarm>, AlarmPutListener> {

    public SetAlarmsTask(SQLiteOpenHelper sqLiteOpenHelper, AlarmPutListener listener) {
        super(sqLiteOpenHelper, listener);
    }


    @Override
    protected ArrayList<Alarm> doInBackground(ArrayList<Alarm>... params) {
        Long time = System.currentTimeMillis();

        ArrayList<Alarm> alarms = params[0];

        SQLiteDatabase db = getWritableDatabase();

        for(Alarm alarm : alarms) {

            alarm.setTimeChanged(time);

            ContentValues values = new ContentValues();
            values.put(Alarm.Entry.COLUMN_ALARM_NAME, alarm.getName());
            values.put(Alarm.Entry.COLUMN_ALARM_HOUR, alarm.getHour());
            values.put(Alarm.Entry.COLUMN_ALARM_MINUTE, alarm.getMinute());
            values.put(Alarm.Entry.COLUMN_ALARM_ENABLED, alarm.getEnabled());
            values.put(Alarm.Entry.COLUMN_ALARM_REPEATED, alarm.getRepeated());
            values.put(Alarm.Entry.COLUMN_ALARM_DAYS, alarm.getDaysString());
            values.put(Alarm.Entry.COLUMN_ALARM_TIME, alarm.getTimeChanged());

            if(alarm.hasId()) {
                String query = Alarm.Entry._ID + " LIKE ?";
                String[] query_values = new String[]{String.valueOf(alarm.getId())};
                int count = db.update(Alarm.Entry.TABLE_NAME, values, query, query_values);

                if(count <= 0) {
                    Log.e("DUONG-UPDATE", "failed to update");
                }
            } else {
                long id = db.insertOrThrow(Alarm.Entry.TABLE_NAME, null, values);
                alarm.setId(id);
            }


        }

        db.close();

        return alarms;
    }

    @Override
    protected void onPostExecute(ArrayList<Alarm> alarms) {
        if(getListener() != null) {
            getListener().onAlarmPut(alarms);
        }

        super.onPostExecute(alarms);
    }
}
