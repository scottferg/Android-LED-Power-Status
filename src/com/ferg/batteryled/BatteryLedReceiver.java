package com.ferg.batteryled;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

public class BatteryLedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context aContext, Intent aIntent) {

        if (aIntent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            aContext.startService(new Intent().setClass(
                aContext, BatteryLedService.class));
        } else {
            aContext.stopService(new Intent().setClass(
                aContext, BatteryLedService.class));
        }
    }
}
