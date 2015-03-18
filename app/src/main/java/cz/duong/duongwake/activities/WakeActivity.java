package cz.duong.duongwake.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;

import cz.duong.duongwake.R;
import cz.duong.duongwake.listeners.AlarmCloseListener;
import cz.duong.duongwake.providers.Alarm;
import cz.duong.duongwake.providers.AlarmManager;
import cz.duong.duongwake.providers.PhoneLock;
import cz.duong.duongwake.ui.AlarmButtonView;


public class WakeActivity extends Activity implements AlarmCloseListener {

    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake);

        if(getIntent() == null || getIntent().getBundleExtra(AlarmManager.INTENT_TAG) == null) {
            Log.d("DUONG-WAKE", "No alarm in intent, shutting down");
            finish();

        } else if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            Log.d("DUONG-WAKE", "Launched from history");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Alarm alarm = getIntent().getBundleExtra(AlarmManager.INTENT_TAG).getParcelable(AlarmManager.INTENT_TAG);

            play(true);
            vibrate(true);
        }



        AlarmButtonView view = (AlarmButtonView) findViewById(R.id.alarm_wake_snooze);
        view.setAlarmCloseListener(this);
    }

    @Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }

    public void play(boolean toggle) {
        if(mPlayer == null) {
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            if(alert == null){ //z nějakého důvodu to bylo null
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }

            try {
                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mPlayer.setDataSource(this, alert);
                mPlayer.setLooping(true);
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(toggle) {
            mPlayer.start();
        } else {
            mPlayer.stop();
        }
    }

    public void vibrate(boolean toggle) {
        Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if(toggle) {
            mVibrator.vibrate(new long[]{0, 1000, 1000}, 0);
        } else {
            mVibrator.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onAlarmClose(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onAlarmClose(true);
    }

    @Override
    public void onAlarmClose(boolean pausing) {
        play(false);
        vibrate(false);

        setIntent(null);

        AlarmManager.setAlarms(this);

        PhoneLock.getInstance(getApplicationContext()).disable();

        if(!pausing) {
            finish();
        }
    }
}
