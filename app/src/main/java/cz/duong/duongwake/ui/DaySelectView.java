package cz.duong.duongwake.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.duong.duongwake.R;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class DaySelectView extends LinearLayout {

    private HashMap<Integer, CheckBox> dayToView = new HashMap<>();
    public DaySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.day_selector, this, true);


        dayToView.put(Calendar.MONDAY, (CheckBox) findViewById(R.id.alarm_add_day_monday));
        dayToView.put(Calendar.TUESDAY, (CheckBox) findViewById(R.id.alarm_add_day_tuesday));
        dayToView.put(Calendar.WEDNESDAY, (CheckBox) findViewById(R.id.alarm_add_day_wednesday));
        dayToView.put(Calendar.THURSDAY, (CheckBox) findViewById(R.id.alarm_add_day_thursday));
        dayToView.put(Calendar.FRIDAY, (CheckBox) findViewById(R.id.alarm_add_day_friday));
        dayToView.put(Calendar.SATURDAY, (CheckBox) findViewById(R.id.alarm_add_day_saturday));
        dayToView.put(Calendar.SUNDAY, (CheckBox) findViewById(R.id.alarm_add_day_sunday));

    }

    public CheckBox getCheckBox(Integer day) {
        return dayToView.get(day);
    }

    public void setDays(ArrayList<Integer> data) {
        for(Integer day = 1; day <= 7; day++) {
            getCheckBox(day).setChecked((data.contains(day)));
        }
    }

    public ArrayList<Integer> getDays() {
        ArrayList<Integer> result = new ArrayList<>();
        for(Map.Entry<Integer, CheckBox> entry : dayToView.entrySet()){
            if(entry.getValue().isChecked()) {
                result.add(entry.getKey());
            }

        }
        return result;
    }
}
