package cz.duong.duongwake.providers;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class PhoneLock {
    private Context mContext;
    public PhoneLock(Context context) {
        mContext = context;
    }

    public void activate() {
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        KeyguardManager km = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");

    }
}
