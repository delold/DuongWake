package cz.duong.duongwake.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cz.duong.duongwake.R;
import cz.duong.duongwake.database.Database;
import cz.duong.duongwake.listeners.AlarmChangeListener;
import cz.duong.duongwake.listeners.DatabaseListener;
import cz.duong.duongwake.providers.Alarm;
import cz.duong.duongwake.providers.AlarmManager;
import cz.duong.duongwake.ui.AddDialog;
import cz.duong.duongwake.ui.AlarmList;


public class MainActivity extends ActionBarActivity implements DatabaseListener, AlarmChangeListener, AdapterView.OnItemClickListener {
    private Database mDb;
    private AlarmList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAdapter = new AlarmList(this, this, new ArrayList<Alarm>());

        ListView view = (ListView) findViewById(R.id.alarm_list);
        view.setAdapter(mAdapter);

        view.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        if(mDb == null) {
            mDb = new Database(this);
        }

        mDb.getAlarms(this);

        setTitle(getString(R.string.alarm_title));

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add) {
            AddDialog dialog = new AddDialog(this);
            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAlarmGet(ArrayList<Alarm> alarm) {
        mAdapter.update(alarm);
    }

    @Override
    public void onAlarmPut(ArrayList<Alarm> a)  {
        AlarmManager.setAlarms(this, mDb);

        mDb.getAlarms(this);
    }

    @Override
    public void onAlarmRemove(Boolean removed) {
        if(removed) {
            AlarmManager.setAlarms(this, mDb);
            mDb.getAlarms(this);
        }
    }

    @Override
    public void onAlarmChange(Alarm a) {
        ArrayList<Alarm> al = new ArrayList<>();
        al.add(a);

        mDb.setAlarms(al, this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        final AlarmList list = (AlarmList) adapterView.getAdapter();
        final MainActivity listener = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(getResources().getStringArray(R.array.dialog_alarm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int action_pos) {
                if(action_pos == 0) {
                    AddDialog dialog = new AddDialog(listener, list.getItem(i));
                    dialog.show();
                } else if (action_pos == 1) {
                    mDb.removeAlarm(list.getItem(i), listener);
                }

            }
        });

        builder.show();
    }


}
