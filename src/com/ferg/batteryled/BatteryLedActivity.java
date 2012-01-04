package com.ferg.batteryled;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.util.Log;

import java.util.ArrayList;

import com.google.ads.*;

public class BatteryLedActivity extends Activity
{
    private static final String TAG = "BatteryLEDActivity";

    private static final int LED_NOTIFICATION = 0;
    private static final int GREEN_SELECTION = 0;
    private static final int YELLOWGREEN_SELECTION = 1;
    private static final int YELLOW_SELECTION = 2;
    private static final int ORANGE_SELECTION = 3;
    private static final int RED_SELECTION = 4;
    private static final int BLUE_SELECTION = 5;
    private static final int CYAN_SELECTION = 6;
    private static final int MAGENTA_SELECTION = 7;
    private static final int WHITE_SELECTION = 8;

    private static final int GREEN_LED = 0xff00ff00;
    private static final int YELLOWGREEN_LED = 0xff66ff00;
    private static final int YELLOW_LED = 0xffffff00;
    private static final int ORANGE_LED = 0xffff4500;
    private static final int RED_LED = 0xffff0000;
    private static final int BLUE_LED = Color.BLUE;
    private static final int CYAN_LED = Color.CYAN;
    private static final int MAGENTA_LED = Color.MAGENTA;
    private static final int WHITE_LED = Color.WHITE;

    private AdView mAdView;
    private ArrayList<ColorPreference> mColorPreferencesList;
    private ListView mColorPreferences;
    private RelativeLayout mAdLayout;

    private int currentPosition = -1;
    
    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.main);

        SharedPreferences p = getSharedPreferences(BatteryLedService.TAG, MODE_PRIVATE);
        mColorPreferencesList = new ArrayList<ColorPreference>();
        mAdLayout = (RelativeLayout) findViewById(R.id.ad_layout);

        mAdView = new AdView(this, AdSize.BANNER, "a14efe38eacd3fa");

        AdRequest adRequest = new AdRequest();
        // adRequest.addTestDevice("82D9965BF398593A478D0DCDA1083E5E");
        // adRequest.addTestDevice("F5CCE6AB5876745848EB6ACACB7FBC01");
        // adRequest.addTestDevice("F6A55CA5B8630B464716A21A100AAA19");

        mAdLayout.addView(mAdView);
        mAdView.loadAd(new AdRequest());

        boolean prefsSet = p.getBoolean(BatteryLedService.PREFS_SET, false);

        if (!prefsSet) {
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.HIGH, GREEN_LED));
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.MEDIUM_HIGH, YELLOWGREEN_LED));
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.MEDIUM, YELLOW_LED));
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.MEDIUM_LOW, ORANGE_LED));
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.LOW, RED_LED));

            for (ColorPreference pref : mColorPreferencesList) {
                updatePreferences(pref.getName(), pref.getColor());
            }
        } else {
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.HIGH, p.getInt(BatteryLedService.HIGH, 0)));
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.MEDIUM_HIGH, p.getInt(BatteryLedService.MEDIUM_HIGH, 0)));
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.MEDIUM, p.getInt(BatteryLedService.MEDIUM, 0)));
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.MEDIUM_LOW, p.getInt(BatteryLedService.MEDIUM_LOW, 0)));
            mColorPreferencesList.add(new ColorPreference(BatteryLedService.LOW, p.getInt(BatteryLedService.LOW, 0)));
        }

        mColorPreferences = (ListView) findViewById(R.id.color_preferences);
        mColorPreferences.setAdapter(new ColorPreferenceAdapter(this, mColorPreferencesList));
        mColorPreferences.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> aParent, View aView, int aPosition, long aId) {
                currentPosition = aPosition;
                registerForContextMenu(aParent);
                openContextMenu(aView);
            }
        });
    }

    public void onCreateContextMenu(ContextMenu aMenu, View aView, ContextMenuInfo aMenuInfo) {
        aMenu.setHeaderTitle("Choose a color");
        aMenu.add(0, GREEN_SELECTION, 0, "Green");
        aMenu.add(0, YELLOWGREEN_SELECTION, 0, "Yellow green");
        aMenu.add(0, YELLOW_SELECTION, 0, "Yellow");
        aMenu.add(0, ORANGE_SELECTION, 0, "Orange");
        aMenu.add(0, RED_SELECTION, 0, "Red");
        aMenu.add(0, BLUE_SELECTION, 0, "Blue");
        aMenu.add(0, CYAN_SELECTION, 0, "Cyan");
        aMenu.add(0, MAGENTA_SELECTION, 0, "Magenta");
        aMenu.add(0, WHITE_SELECTION, 0, "White");
    }

    @Override
    public boolean onContextItemSelected(MenuItem aMenuItem) {
        ColorPreferenceAdapter colorPrefAdapter = (ColorPreferenceAdapter) mColorPreferences.getAdapter();
        ColorPreference selectedPreference = (ColorPreference) colorPrefAdapter.getItem(currentPosition);
        
        int result = GREEN_LED;

        switch(aMenuItem.getItemId()) {
            case GREEN_SELECTION:
                result = GREEN_LED;
                break;
            case YELLOWGREEN_SELECTION:
                result = YELLOWGREEN_LED;
                break;
            case YELLOW_SELECTION:
                result = YELLOW_LED;
                break;
            case ORANGE_SELECTION:
                result = ORANGE_LED;
                break;
            case RED_SELECTION:
                result = RED_LED;
                break;
            case BLUE_SELECTION:
                result = BLUE_LED;
                break;
            case CYAN_SELECTION:
                result = CYAN_LED;
                break;
            case MAGENTA_SELECTION:
                result = MAGENTA_LED;
                break;
            case WHITE_SELECTION:
                result = WHITE_LED;
                break;
        }

        mColorPreferencesList.set(currentPosition, new ColorPreference(selectedPreference.getName(), result));

        colorPrefAdapter.notifyDataSetChanged();
        mColorPreferences = (ListView) mColorPreferences;
        
        updatePreferences(selectedPreference.getName(), result);

        return true;
    }

    public void onContextMenuClosed(Menu aMenu) {
        unregisterForContextMenu(mColorPreferences);
    }

    public void updatePreferences(String aPrefName, int aColor) {
        Editor editor = getSharedPreferences(BatteryLedService.TAG, MODE_PRIVATE).edit();
        editor.putInt(aPrefName, aColor);
        editor.putBoolean(BatteryLedService.PREFS_SET, true);
        editor.commit();
    }

    public void colorNotify(int aColor) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification flash = new Notification();

        flash.ledARGB = RED_LED;

        flash.flags = Notification.FLAG_SHOW_LIGHTS|Notification.FLAG_ONGOING_EVENT;
        flash.ledOnMS = 1000; 
        flash.ledOffMS = 50; 

        notificationManager.notify(LED_NOTIFICATION, flash);
    }

    private class ColorPreferenceAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private ArrayList<ColorPreference> mItems;

        public ColorPreferenceAdapter(Context aContext, ArrayList<ColorPreference> aItems) {
            mContext = aContext;
            mInflater = LayoutInflater.from(mContext);
            mItems = aItems;
        }

        public View getView(int aPosition, View aConvertView, ViewGroup aParent) {
            View inflatedView;
            
            if (aConvertView == null) {
                inflatedView = mInflater.inflate(R.layout.color_item, null);
            } else {
                inflatedView = aConvertView;
            }

            TextView name = (TextView) inflatedView.findViewById(R.id.value_name);
            TextView caption = (TextView) inflatedView.findViewById(R.id.value_caption);
            ImageView icon = (ImageView) inflatedView.findViewById(R.id.value_preview);

            String captionText;
            String valueName = mItems.get(aPosition).getName();

            if (valueName.equals("High")) {
                captionText = getString(R.string.high);
            } else if (valueName.equals("Medium-high")) {
                captionText = getString(R.string.medium_high);
            } else if (valueName.equals("Medium")) {
                captionText = getString(R.string.medium);
            } else if (valueName.equals("Medium-low")) {
                captionText = getString(R.string.medium_low);
            } else { 
                captionText = getString(R.string.low);
            }

            icon.setColorFilter(mItems.get(aPosition).getColor(), Mode.MULTIPLY);
            name.setText(valueName);
            caption.setText(captionText);

            return inflatedView;
        }

        public int getCount() {
            return mItems.size();
        }

        public Object getItem(int aPosition) {
            return mItems.get(aPosition);
        }

        public long getItemId(int aPosition) {
            return aPosition;
        }
    }

    private class ColorPreference {
        private String mName;
        private int mColor;

        public ColorPreference(String aName, int aColor) {
            mName = aName;
            mColor = aColor;
        }

        public String getName() {
            return mName;
        }

        public int getColor() {
            return mColor;
        }
    }
}
