package cz.duong.duongwake.listeners;

import java.util.ArrayList;

import cz.duong.duongwake.Alarm;

/**
 * Vytvořeno David on 14. 3. 2015.
 */
public class DatabaseListeners {
    public static interface AlarmGetListener {
        public void onAlarmGet(ArrayList<Alarm> alarm);
    }

    public static interface AlarmPutListener {
        public void onAlarmPut();
    }

    public static interface PendingsListener {
        public void onPendingCleared();
    }

}
