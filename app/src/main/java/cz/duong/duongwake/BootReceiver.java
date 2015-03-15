package cz.duong.duongwake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cz.duong.duongwake.database.Database;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager.setAlarms(context, new Database(context));
    }
}
