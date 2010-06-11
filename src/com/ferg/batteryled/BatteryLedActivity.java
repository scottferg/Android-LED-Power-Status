package com.ferg.batteryled;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.util.Log;

public class BatteryLedActivity extends Activity
{
    private static final String TAG = "BatteryLEDActivity";
    private static final int LED_NOTIFICATION = 0;
    private static final int GREEN = 0;
    private static final int YELLOWGREEN = 1;
    private static final int YELLOW = 2;
    private static final int ORANGE = 3;
    private static final int RED = 4;
    private static final int GREEN_LED = 0xff00ff00;
    private static final int YELLOWGREEN_LED = 0xff66ff00;
    private static final int YELLOW_LED = 0xffffff00;
    private static final int ORANGE_LED = 0xffff4500;
    private static final int RED_LED = 0xffff0000;

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.main);

        Button green = (Button) findViewById(R.id.green);
        Button yellowGreen = (Button) findViewById(R.id.palegreen);
        Button yellow = (Button) findViewById(R.id.yellow);
        Button orange = (Button) findViewById(R.id.orange);
        Button red = (Button) findViewById(R.id.red);

        green.setOnClickListener(new View.OnClickListener() {
            public void onClick(View aView) {
                Log.i(TAG, "Green clicked!");
                colorNotify(GREEN);
            }
        });

        yellowGreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View aView) {
                colorNotify(YELLOWGREEN);
            }
        });

        yellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View aView) {
                colorNotify(YELLOW);
            }
        });

        orange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View aView) {
                colorNotify(ORANGE);
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            public void onClick(View aView) {
                colorNotify(RED);
            }
        });
    }

    public void colorNotify(int aColor) {

        Log.i(TAG, Integer.toString(aColor));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification flash = new Notification();

        switch (aColor) {
            case GREEN:
                flash.ledARGB = GREEN_LED;
                break;
            case YELLOWGREEN:
                flash.ledARGB = YELLOWGREEN_LED;
                break;
            case YELLOW:
                flash.ledARGB = YELLOW_LED;
                break;
            case ORANGE:
                flash.ledARGB = ORANGE_LED;
                break;
            case RED:
                flash.ledARGB = RED_LED;
                break;
        }

        flash.flags = Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_ONGOING_EVENT;
        flash.ledOnMS = 1000; 
        flash.ledOffMS = 10; 

        notificationManager.notify(LED_NOTIFICATION, flash);
    }
}
