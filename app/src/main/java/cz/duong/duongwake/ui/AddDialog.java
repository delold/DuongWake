package cz.duong.duongwake.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import cz.duong.duongwake.providers.Alarm;
import cz.duong.duongwake.R;
import cz.duong.duongwake.listeners.AlarmChangeListener;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class AddDialog extends Dialog implements View.OnClickListener {

    private Alarm mAlarm;
    private AlarmChangeListener mListener;

    public AddDialog(Context context, AlarmChangeListener listener, Alarm a) {
        super(context);

        mListener = listener;
        mAlarm = a;

        if(a == null) {
            Calendar cal = Calendar.getInstance();
            String name = context.getString(R.string.alarm_default);

            mAlarm = new Alarm(name, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), "", true, false, 0L);
        }
    }

    public AddDialog(AlarmChangeListener listener) {
        this((Context) listener, listener, null);
    }
    public AddDialog(AlarmChangeListener listener, Alarm a) {
        this((Context) listener, listener, a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_alarm);

        TabHost tabHost = (TabHost) findViewById(R.id.alarm_add_tabhost);
        tabHost.setup();

        tabHost.addTab(bindTime(tabHost));
        tabHost.addTab(bindInfo(tabHost));

        Button button = (Button) findViewById(R.id.alarm_add_button);
        button.setOnClickListener(this);
    }

    private View constructIndicator(String label) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_tab_item, null);

        TextView text = (TextView) view.findViewById(R.id.alarm_add_tab_item);
        text.setText(label);

        return view;
    }

    private TabHost.TabSpec bindTime(TabHost tabHost) {
        String label = getContext().getString(R.string.editor_tab_time);

        TabHost.TabSpec time_spec = tabHost.newTabSpec(label);

        time_spec.setIndicator(constructIndicator(label));

        time_spec.setContent(R.id.alarm_add_timepicker);

        TimePicker picker = (TimePicker) findViewById(R.id.alarm_add_timepicker);
        picker.setIs24HourView(true);

        picker.setCurrentHour(mAlarm.getHour());
        picker.setCurrentMinute(mAlarm.getMinute());

        return time_spec;
    }

    private TabHost.TabSpec bindInfo(TabHost tabHost) {
        String label = getContext().getString(R.string.editor_tab_info);
        TabHost.TabSpec info_spec = tabHost.newTabSpec(label);

        info_spec.setIndicator(constructIndicator(label));
        info_spec.setContent(R.id.alarm_add_info);

        EditText name = (EditText) findViewById(R.id.alarm_add_name);
        DaySelectView daySelect = (DaySelectView) findViewById(R.id.alarm_add_daypicker);
        SwitchCompat enabled = (SwitchCompat) findViewById(R.id.alarm_add_enabled);

        name.setText(mAlarm.getName());
        enabled.setChecked(mAlarm.isEnabled());
        daySelect.setDays(mAlarm.getDays());

        return info_spec;
    }

    @Override
    public void onClick(View view) {
        TimePicker picker = (TimePicker) findViewById(R.id.alarm_add_timepicker);
        EditText name = (EditText) findViewById(R.id.alarm_add_name);
        DaySelectView daySelect = (DaySelectView) findViewById(R.id.alarm_add_daypicker);
        SwitchCompat enabled = (SwitchCompat) findViewById(R.id.alarm_add_enabled);

        mAlarm.setDays(daySelect.getDays());
        mAlarm.setName(name.getText().toString());
        mAlarm.setEnabled(enabled.isChecked());
        mAlarm.setHour(picker.getCurrentHour());
        mAlarm.setMinute(picker.getCurrentMinute());

        mListener.onAlarmChange(mAlarm);

        dismiss();
    }
}
