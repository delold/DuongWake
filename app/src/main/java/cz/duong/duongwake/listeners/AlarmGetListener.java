package cz.duong.duongwake.listeners;

import java.util.ArrayList;

import cz.duong.duongwake.providers.Alarm;

/**
 * Vytvořeno David on 16. 3. 2015.
 */
public interface AlarmGetListener {
    public void onAlarmGet(ArrayList<Alarm> alarm);
}
