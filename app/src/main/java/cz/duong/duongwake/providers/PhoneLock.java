package cz.duong.duongwake.providers;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

/**
 * Vytvořeno David on 15. 3. 2015.
 */
public class PhoneLock {
    private static PhoneLock mInstance;

    private KeyguardManager mKeyGuard;
    private PowerManager mPowerManager;

    private PowerManager.WakeLock mWakeLock;
    private KeyguardManager.KeyguardLock mKeyLock;



    private static String LOCK_TAG = "DUONG-WAKE";

    public PhoneLock(Context context) {
        mKeyGuard = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    //Abych uchoval reference k jednotlivým zámkům (locks)
    public static PhoneLock getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new PhoneLock(context);
        }

        return mInstance;
    }

    private KeyguardManager.KeyguardLock getKeyLock() {
        if(mKeyLock == null) {
            mKeyLock = mKeyGuard.newKeyguardLock(LOCK_TAG);
        }
        return mKeyLock;
    }

    private PowerManager.WakeLock getWakelock() {
        if(mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, LOCK_TAG);
        }
        return mWakeLock;
    }

    public void activate() {
        getKeyLock().disableKeyguard();
        getWakelock().acquire();
    }

    public void disable() {
        getKeyLock().reenableKeyguard();

        if(getWakelock().isHeld()) {
            getWakelock().release();
        }

    }
}
