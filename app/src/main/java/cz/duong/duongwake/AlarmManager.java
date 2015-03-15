package cz.duong.duongwake;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import cz.duong.duongwake.database.Database;
import cz.duong.duongwake.listeners.DatabaseListeners;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class AlarmManager {
    public static final int INTENT_ID = 354269;
    public static final String INTENT_TAG = "alarmitem";

    public static void setAlarms(Context context, ArrayList<Alarm> alarms) {
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a", Locale.ENGLISH);

        Log.d("DUONG-SETALARM", "setting alarm at: " + format.format(Calendar.getInstance().getTime()));
        android.app.AlarmManager manager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();

        Long min = 0L;
        Alarm min_alarm = null;
        for(Alarm a : alarms) {
            Long time = a.getTimestamp(cal);
            if(time != null && (min == 0L || (time < min))) {
                min = time;
                min_alarm = a;
            }
        }

        if(min_alarm != null){
            PendingIntent pi = createPending(context, min_alarm);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                manager.setExact(android.app.AlarmManager.RTC_WAKEUP, min, pi);
            } else {
                manager.set(android.app.AlarmManager.RTC_WAKEUP, min, pi);
            }

            Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
            alarmChanged.putExtra("alarmSet", true);
            context.sendBroadcast(alarmChanged);

            Toast.makeText(context, "Set Alarm", Toast.LENGTH_LONG).show();
        } else {
            Log.d("DUONG-WAKE", "žádné alarmy");
        }
    }

    public static PendingIntent createPending(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(INTENT_TAG, alarm);

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
            db.getAlarms(new DatabaseListeners.AlarmGetListener() {
                @Override
                public void onAlarmGet(ArrayList<Alarm> alarm) {
                    setAlarms(context, db);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void clearPendings(final Context context, final Database db) {
        //než abych zakládal další listener pro tak minuskulární operaci, hodím to do sync, doba na čekání je minimální
        try {
            db.getAlarms(new DatabaseListeners.AlarmGetListener() {
                @Override
                public void onAlarmGet(ArrayList<Alarm> alarm) {
                    clearPendings(context, db);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
