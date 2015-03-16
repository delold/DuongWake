package cz.duong.duongwake.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TimePicker;

import java.util.Calendar;

import cz.duong.duongwake.providers.Alarm;
import cz.duong.duongwake.R;
import cz.duong.duongwake.listeners.DialogListener;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class AddDialog extends Dialog implements View.OnClickListener {

    private Alarm mAlarm;
    private DialogListener mListener;

    public AddDialog(Context context, DialogListener listener, Alarm a) {
        super(context);

        mListener = listener;
        mAlarm = a;

        if(a == null) {
            Calendar cal = Calendar.getInstance();
            //TODO: STRINGY
            mAlarm = new Alarm("Alarm", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), "", true, false);
        }
    }

    public AddDialog(DialogListener listener) {
        this((Context) listener, listener, null);
    }
    public AddDialog(DialogListener listener, Alarm a) {
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

    private TabHost.TabSpec bindTime(TabHost tabHost) {
        //TODO: STRINGY
        TabHost.TabSpec time_spec = tabHost.newTabSpec("Čas");

        time_spec.setIndicator("Čas");
        time_spec.setContent(R.id.alarm_add_timepicker);

        TimePicker picker = (TimePicker) findViewById(R.id.alarm_add_timepicker);
        picker.setIs24HourView(true);

        picker.setCurrentHour(mAlarm.getHour());
        picker.setCurrentMinute(mAlarm.getMinute());

        return time_spec;
    }

    private TabHost.TabSpec bindInfo(TabHost tabHost) {
        //TODO: STRINGY
        TabHost.TabSpec info_spec = tabHost.newTabSpec("Info");

        info_spec.setIndicator("Info");
        info_spec.setContent(R.id.alarm_add_info);

        EditText name = (EditText) findViewById(R.id.alarm_add_name);
        DaySelectView daySelect = (DaySelectView) findViewById(R.id.alarm_add_daypicker);
        Switch enabled = (Switch) findViewById(R.id.alarm_add_enabled);

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
        Switch enabled = (Switch) findViewById(R.id.alarm_add_enabled);

        mAlarm.setDays(daySelect.getDays());
        mAlarm.setName(name.getText().toString());
        mAlarm.setEnabled(enabled.isEnabled());
        mAlarm.setHour(picker.getCurrentHour());
        mAlarm.setMinute(picker.getCurrentMinute());

        mListener.onDialogDone(mAlarm);

        dismiss();
    }
}
