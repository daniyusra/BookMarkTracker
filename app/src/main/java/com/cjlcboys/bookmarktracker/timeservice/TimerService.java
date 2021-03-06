package com.cjlcboys.bookmarktracker.timeservice;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.cjlcboys.bookmarktracker.Helper;
import com.cjlcboys.bookmarktracker.MainActivity;
import com.cjlcboys.bookmarktracker.R;
import com.cjlcboys.bookmarktracker.bookmarkrecyclerview.Bookmark;

public class TimerService extends Service {

    private static final String CHANNEL_ID = "cjlc_boys_channel_id";
    private List<Bookmark> mBookmarks;

    public static String str_receiver = "com.cjlcboys.bookmarktracker.timeservice.receive";

    private Handler mHandler = new Handler();
    Calendar calendar;
    private Timer mTimer = null;
    public static final long NOTIFY_INTERVAL = 10000;
    Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("LOG","creating a service");

        calendar = Calendar.getInstance();
        mBookmarks = new ArrayList<>();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("LOG","External Storage is loaded");
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS), Helper.BOOKMARKS_FILE_NAME);
            try {
                Helper.load_bookmarks(mBookmarks,file,true);
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.i("LOG","No file found/Error parsing file. Bookmarks will not be loaded");
            }
        }
        else{
            Log.i("LOG","External Storage is not loaded. Bookmarks will not be loaded");
        }
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);
        intent = new Intent(str_receiver);
    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    calendar = Calendar.getInstance();
                    twoDatesBetweenTime();
                }

            });
        }

    }

    public void twoDatesBetweenTime() {
        try {
            for(Bookmark bmark: mBookmarks) {
                if (!bmark.isReminder()){
                    continue;
                }
                if (calendar.getTime().getTime()>bmark.getEndTime().getTime()){
                    bmark.setReminder(false);
                    Log.i("LOG","Pushing a reminder");
                    NotificationManager notificationManager = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                    Notification.Builder n  = new Notification.Builder(this)
                            .setContentTitle("Reminder! Time to check this out")
                            .setContentText(bmark.getTitle())
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        NotificationChannel channel = new NotificationChannel(
                                CHANNEL_ID,
                                "Atode",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(channel);
                        n.setChannelId(CHANNEL_ID);
                    }
                    notificationManager.notify(0, n.build());
                }
            }
        } catch (Exception e) {
            mTimer.cancel();
            mTimer.purge();
            Log.i("LOG",e.getMessage());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service finish","Finish");
    }


}
