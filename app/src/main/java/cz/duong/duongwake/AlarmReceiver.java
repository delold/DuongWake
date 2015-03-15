package cz.duong.duongwake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cz.duong.duongwake.activities.WakeActivity;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Starting service", Toast.LENGTH_LONG).show();

        String tag = cz.duong.duongwake.AlarmManager.INTENT_TAG;

        Intent act = new Intent(context, WakeActivity.class);
        act.putExtra(tag, intent.getExtras().getParcelable(tag)); //předej data
        act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);

        context.startActivity(act);

    }
}
