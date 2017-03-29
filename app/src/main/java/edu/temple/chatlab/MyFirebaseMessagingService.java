package edu.temple.chatlab;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String PREFS_NAME = "LogFile.xml";
    String messageCount = "Count";
    SharedPreferences logFile;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        Long millisecondTime = remoteMessage.getSentTime();

        Intent intent = new Intent("android.intent.action.MAIN")
                .putExtra("incoming_message", remoteMessage.getNotification().getBody() +
                " - " + getDate(millisecondTime));

        this.sendBroadcast(intent);
        this.stopSelf();

    }

    public static String getDate(long milliSeconds)
    {
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(milliSeconds);
        String date = "" + cl.get(Calendar.MONTH) + "/" + cl.get(Calendar.DAY_OF_MONTH) + "/" + cl.get(Calendar.YEAR);
        String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
        String dateAndTime = date + " " + time;
        return dateAndTime;
    }

}
