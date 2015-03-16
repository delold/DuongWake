package cz.duong.duongwake.providers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import cz.duong.duongwake.database.Database;
import cz.duong.duongwake.listeners.AlarmGetListener;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class AlarmManager {
    public static final int INTENT_ID = 354269;
    public static final String INTENT_TAG = "alarmitem";

    public static void setAlarms(Context context, ArrayList<Alarm> alarms) {

        //odstraň všechny přebývající alarmy
        clearPendings(context, alarms);

        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.ENGLISH);
        Log.d("DUONG-SETALARM", "setting alarm at: " + format.format(Calendar.getInstance().getTime()));


        Calendar calendar = Calendar.getInstance();

        Alarm alarm = findClosestAlarm(calendar, alarms);

        if(alarm != null){
            assignAlarm(context, alarm, calendar);
        } else {
            Log.d("DUONG-WAKE", "Žádné alarmy");
        }
    }

    public static void assignAlarm(Context context, Alarm alarm, Calendar calendar) {
        android.app.AlarmManager manager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent intent = createPending(context, alarm);
        Long timestamp = alarm.getTimestamp(calendar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            android.app.AlarmManager.AlarmClockInfo info = new android.app.AlarmManager.AlarmClockInfo(timestamp, intent);
            manager.setAlarmClock(info, intent);

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(android.app.AlarmManager.RTC_WAKEUP, timestamp, intent);
            } else {
                manager.set(android.app.AlarmManager.RTC_WAKEUP, timestamp, intent);
            }

            Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
            alarmChanged.putExtra("alarmSet", true);
            context.sendBroadcast(alarmChanged);
        }

        Toast.makeText(context, "Set Alarm", Toast.LENGTH_LONG).show();
    }

    public static Alarm findClosestAlarm(Calendar cal, ArrayList<Alarm> alarms) {
        Long min = 0L;
        Alarm min_alarm = null;
        for(Alarm alarm : alarms) {
            Long time = alarm.getTimestamp(cal);

            if(time != null && (min == 0L || (time < min))) {
                min = time;
                min_alarm = alarm;
            }
        }

        return min_alarm;
    }

    public static PendingIntent createPending(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        //https://code.google.com/p/android/issues/detail?id=6822
        Bundle bundle = new Bundle();
        bundle.putParcelable(INTENT_TAG, alarm);

        intent.putExtra(INTENT_TAG, bundle);

        return PendingIntent.getBroadcast(context, INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void clearPendings(Context context, ArrayList<Alarm> alarms) {
        android.app.AlarmManager manager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for(Alarm a : alarms) {
            manager.cancel(AlarmManager.createPending(context, a));
        }
    }

    public static void setAlarms(final Context context, final Database db) {
        try {
            db.getAlarms(new AlarmGetListener() {
                @Override
                public void onAlarmGet(ArrayList<Alarm> alarm) {
                    setAlarms(context, alarm);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void setAlarms(Context context) {
        setAlarms(context, new Database(context));
    }
}
