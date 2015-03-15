package cz.duong.duongwake.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import cz.duong.duongwake.ui.AddDialog;
import cz.duong.duongwake.Alarm;
import cz.duong.duongwake.ui.AlarmList;
import cz.duong.duongwake.R;
import cz.duong.duongwake.database.Database;
import cz.duong.duongwake.listeners.DatabaseListeners;
import cz.duong.duongwake.listeners.DialogListener;


public class MainActivity extends ActionBarActivity implements DatabaseListeners.AlarmGetListener, DialogListener, DatabaseListeners.AlarmPutListener {

    private Database mDb;
    private AlarmList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAdapter = new AlarmList(this, new ArrayList<Alarm>());

        ListView view = (ListView) findViewById(R.id.alarm_list);
        view.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        if(mDb == null) {
            mDb = new Database(this);
        }


        mDb.getAlarms(this);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_add) {
            AddDialog dialog = new AddDialog(this);
            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onAlarmGet(ArrayList<Alarm> alarm) {
        Calendar cal = Calendar.getInstance();
        for(Alarm a : alarm) {

            Log.d("DUONG-ALARM", a.getName() + ": -> " + a.getTimestamp(cal));
        }

        mAdapter.update(alarm);

    }

    @Override
    public void onAlarmPut() {
        mDb.getAlarms(this);
    }

    @Override
    public void onDialogDone(Alarm a) {
        //TODO: přidat editaci

        ArrayList<Alarm> al = new ArrayList<>();
        al.add(a);

        mDb.setAlarm(al, this);
    }


}
