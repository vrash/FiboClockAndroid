package com.vrashabh.fiboclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class FiboMainActivity extends Activity {

    public final static String[] hourArray = {" ", "bc", "ac", "bd", "cd", "acd", "abcd", "de",
            "abce", "abde", "acde", "abcde"};
    public final static String[] minArray = {" ", "a", "ab", "d", "abc", "e", "be", "ce",
            "ace", "bde", "cde", "acde", "abcde"};
    LinkedHashMap<Character, Integer> counterClock = new LinkedHashMap<>();
    LinkedHashMap<Integer, Integer> rectState = new LinkedHashMap<>();
    View oneSqaure;
    View oneOthersqaure;
    View twoSquare;
    View threeSquare;
    View fiveSquare;
    TextView testTimer;
    int curHour;
    int curMinute;
    boolean isTimeShown = false;
    String curHourObj;
    String curMinObj;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_TIME_TICK)) {
                generateFiboColourClock();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibo_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        IntentFilter s_intentFilter;
        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        oneSqaure = (View) findViewById(R.id.onesqaure);
        oneOthersqaure = (View) findViewById(R.id.otheronesquare);
        twoSquare = (View) findViewById(R.id.twosquare);
        threeSquare = (View) findViewById(R.id.threesquare);
        fiveSquare = (View) findViewById(R.id.fivesquare);
        registerReceiver(m_timeChangedReceiver, s_intentFilter);
        generateFiboColourClock();


    }

    public void InitializeMaps() {
        counterClock.put('a', 0);
        counterClock.put('b', 0);
        counterClock.put('c', 0);
        counterClock.put('d', 0);
        counterClock.put('e', 0);
        rectState.put(0, R.color.gray);
        rectState.put(1, R.color.red);
        rectState.put(2, R.color.lime);
        rectState.put(3, R.color.blue);
    }

    public void generateFiboColourClock() {
        //Initialize/Reset the hashmaps
        InitializeMaps();
        Calendar cal = Calendar.getInstance();
        curMinute = cal.get(Calendar.MINUTE);
        curHour = cal.get(Calendar.HOUR);
        curHourObj = hourArray[curHour % 12];
        curMinObj = minArray[curMinute / 5];
        for (int i = 0; i < curHourObj.length(); i++) {
            if (!curHourObj.isEmpty())
                counterClock.put(curHourObj.charAt(i), counterClock.get(curHourObj.charAt(i)) + 1);
        }
        for (int i = 0; i < curMinObj.length(); i++) {
            if (!curMinObj.isEmpty()) {
                counterClock.put(curMinObj.charAt(i), counterClock.get(curMinObj.charAt(i)) + 1);
                counterClock.put(curMinObj.charAt(i), counterClock.get(curMinObj.charAt(i)) + 1);
            }
        }

        for (Map.Entry<Character, Integer> entry : counterClock.entrySet()) {
            switch (entry.getKey()) {
                case 'a':
                    GradientDrawable background = (GradientDrawable) oneSqaure.getBackground();
                    background.setColor(getResources().getColor(rectState.get(entry.getValue())));
                    break;
                case 'b':
                    GradientDrawable background1 = (GradientDrawable) oneOthersqaure.getBackground();
                    background1.setColor(getResources().getColor(rectState.get(entry.getValue())));
                    break;
                case 'c':
                    GradientDrawable background2 = (GradientDrawable) twoSquare.getBackground();
                    background2.setColor(getResources().getColor(rectState.get(entry.getValue())));

                    break;
                case 'd':
                    GradientDrawable background3 = (GradientDrawable) threeSquare.getBackground();
                    background3.setColor(getResources().getColor(rectState.get(entry.getValue())));
                    break;
                case 'e':
                    GradientDrawable background4 = (GradientDrawable) fiveSquare.getBackground();
                    background4.setColor(getResources().getColor(rectState.get(entry.getValue())));
                    break;
            }
        }


    }

    public void showTime(View v) {
        testTimer = (TextView) findViewById(R.id.text_current_time);
        if (isTimeShown) {
            testTimer.setVisibility(View.GONE);

        } else {
            testTimer.setVisibility(View.VISIBLE);
            testTimer.setText(curHour + ":" + curMinute);

        }
        isTimeShown = !isTimeShown;

    }

    public void shareOnFB(View v) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("www.whizmodo.wordpress.com"))
                    .setContentDescription("Do you know how to view the fibonacci time? Get your geek on now!")
                    .setImageUrl(Uri.parse("https://ksr-ugc.imgix.net/projects/1763997/photo-original.jpg?v=1429121202&w=1024&h=768&fit=crop&auto=format&q=92&s=7a3ad43d1417131a924e1eea91a93833"))
                    .setContentTitle("Fibonacci clock for Android!")
                    .build();
            shareDialog.show(content);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                //newGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
