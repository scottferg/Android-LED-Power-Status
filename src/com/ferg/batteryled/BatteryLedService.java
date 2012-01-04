package com.ferg.batteryled;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class BatteryLedService extends Service {

    public static final String TAG = "BatteryLEDService";

    private static final int LED_NOTIFICATION = 0;

    private static int HIGH_COLOR = 0xff00ff00;
    private static int MEDIUM_HIGH_COLOR = 0xff66ff00;
    private static int MEDIUM_COLOR = 0xffffff00;
    private static int MEDIUM_LOW_COLOR = 0xffff4500;
    private static int LOW_COLOR = 0xffff0000;

    public static final String HIGH = "High";
    public static final String MEDIUM_HIGH = "Medium-high";
    public static final String MEDIUM = "Medium";
    public static final String MEDIUM_LOW = "Medium-low";
    public static final String LOW = "Low";
    public static final String PREFS_SET = "PrefSet";

    private NotificationManager mNotificationManager;
    private BroadcastReceiver mBatteryInfoReceiver;
    private SharedPreferences mPrefs;

    @Override
    public void onCreate() {

        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mPrefs = getSharedPreferences(TAG, MODE_PRIVATE);

        mBatteryInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {

                int level = intent.getIntExtra("level", 0);

                if (mPrefs.getBoolean(PREFS_SET, false)) {
                    HIGH_COLOR = mPrefs.getInt(HIGH, 0);
                    MEDIUM_HIGH_COLOR = mPrefs.getInt(MEDIUM_HIGH, 0);
                    MEDIUM_COLOR = mPrefs.getInt(MEDIUM, 0);
                    MEDIUM_LOW_COLOR = mPrefs.getInt(MEDIUM_LOW, 0);
                    LOW_COLOR = mPrefs.getInt(LOW, 0);
                }

                Log.i(TAG, Integer.toString(level));

                Notification flash = new Notification();
                mNotificationManager.cancelAll();

                if (level < 25) {
                    flash.ledARGB = LOW_COLOR;
                } else if (level > 24 && level < 50) {
                    flash.ledARGB = MEDIUM_LOW_COLOR;
                } else if (level > 49 && level < 75) {
                    flash.ledARGB = MEDIUM_COLOR;
                } else if (level > 74 && level < 90) {
                    flash.ledARGB = MEDIUM_HIGH_COLOR;
                } else {
                    flash.ledARGB = HIGH_COLOR;
                }

                flash.flags = Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_ONGOING_EVENT;
                flash.ledOnMS = 100; 
                flash.ledOffMS = 100; 
                mNotificationManager.notify(LED_NOTIFICATION, flash);
            }
        };

        this.registerReceiver(this.mBatteryInfoReceiver, 
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        mNotificationManager.cancelAll();
    }

    public IBinder onBind(Intent aIntent) {

        return null;
    }
}
