package edu.temple.chatlab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LogFile.xml";
    String messageCount = "Count";

    LinearLayout scrollingLinearLayout;
    Boolean colorSwitcher;
    String mostRecentMessage;

    private Context activityContext;

    BroadcastReceiver messageReceiver;
    SharedPreferences logFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityContext = this;
        colorSwitcher = true;

        logFile = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String messageCountString = logFile.getString(messageCount, "0");
        int count = Integer.parseInt(messageCountString);

        String messageKey = "";
        String messageToAdd = "";
        for (int i = 1; i <= count; i++) {

            messageKey = String.valueOf(i);
            messageToAdd = logFile.getString(messageKey, "DEFAULT");
            addMessage(messageToAdd);

        }

    }

    @Override
    protected void onResume() {

        super.onResume();

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");

        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                mostRecentMessage = intent.getStringExtra("incoming_message");
                Log.d("Message", mostRecentMessage);

                logNewMessage(mostRecentMessage, activityContext);
                addMessage(mostRecentMessage);

            }
        };

        this.registerReceiver(messageReceiver, intentFilter);

    }

    @Override
    protected void onPause() {

        super.onPause();

        this.unregisterReceiver(this.messageReceiver);

    }

    private void addMessage (String messageToAdd) {

        scrollingLinearLayout = (LinearLayout) findViewById(R.id.scrollingLinearLayout);

        TextView textView = new TextView(this);
        textView.setText(messageToAdd);
        textView.setTextSize(20);
        textView.setPadding(20, 20, 20, 20);
        textView.setTextColor(ContextCompat.getColor(activityContext, R.color.messageBorder));
        textView.setBackground(ContextCompat.getDrawable(activityContext, R.drawable.box));

        scrollingLinearLayout.addView(textView);

    }

    private void logNewMessage (String messageToAdd, Context c) {

        String messageCountString = logFile.getString(messageCount, "0");
        int currentMessageCount = Integer.parseInt(messageCountString);
        currentMessageCount++;

        SharedPreferences.Editor editor = logFile.edit();
        editor.putString(Integer.toString(currentMessageCount), messageToAdd);
        editor.putString(messageCount, Integer.toString(currentMessageCount));
        editor.commit();
        Log.d("Message Logged", "#" + currentMessageCount + ": " + messageToAdd);

    }

}
