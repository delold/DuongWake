package cz.duong.duongwake.database.tasks;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cz.duong.duongwake.listeners.AlarmRemoveListener;
import cz.duong.duongwake.providers.Alarm;

/**
 * Vytvořeno David on 17. 3. 2015.
 */
public class RemoveAlarmTask extends DatabaseTask<Alarm, Boolean, AlarmRemoveListener> {
    public RemoveAlarmTask(SQLiteOpenHelper sqLiteOpenHelper, AlarmRemoveListener alarmRemoveListener) {
        super(sqLiteOpenHelper, alarmRemoveListener);
    }

    @Override
    protected Boolean doInBackground(Alarm... params) {
        SQLiteDatabase db = getReadableDatabase();

        Alarm alarm = params[0];

        String query = Alarm.Entry._ID + " LIKE ?";
        String[] query_values = new String[]{String.valueOf(alarm.getId())};

        int count = db.delete(Alarm.Entry.TABLE_NAME, query, query_values);

        db.close();

        return count >= 1;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        getListener().onAlarmRemove(aBoolean);
    }
}
