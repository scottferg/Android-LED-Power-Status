package com.ferg.batteryled;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class BatteryLedService extends Service {

    private static final String TAG = "BatteryLED";

    private static final int LED_NOTIFICATION = 0;

    private static final int GREEN_LED = 0xff00ff00;
    private static final int YELLOWGREEN_LED = 0xff66ff00;
    private static final int YELLOW_LED = 0xffffff00;
    private static final int ORANGE_LED = 0xffff4500;
    private static final int RED_LED = 0xffff0000;

    private NotificationManager mNotificationManager;
    private BroadcastReceiver mBatteryInfoReceiver;

    @Override
    public void onCreate() {

        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBatteryInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {

                int level = intent.getIntExtra("level", 0);

                Log.i(TAG, Integer.toString(level));

                Notification flash = new Notification();
                mNotificationManager.cancelAll();

                if (level < 25) {
                    flash.ledARGB = RED_LED;
                } else if (level > 24 && level < 50) {
                    flash.ledARGB = ORANGE_LED;
                } else if (level > 49 && level < 75) {
                    flash.ledARGB = YELLOW_LED;
                } else if (level > 74 && level < 90) {
                    flash.ledARGB = YELLOWGREEN_LED;
                } else {
                    flash.ledARGB = GREEN_LED;
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
