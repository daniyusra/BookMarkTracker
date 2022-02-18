package com.cjlcboys.bookmarktracker.timeservice;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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

    private List<Bookmark> mBookmarks;

    public static String str_receiver = "com.cjlcboys.bookmarktracker.timeservice.receive";

    private Handler mHandler = new Handler();
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String strDate;
    Date date_current;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;

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

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        mBookmarks = new ArrayList<>();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("LOG","External Storage is loaded");
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS), Helper.BOOKMARKS_FILE_NAME);
            try {
                Helper.load_bookmarks(mBookmarks,file);
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
                    simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    strDate = simpleDateFormat.format(calendar.getTime());
                    Log.e("strDate", strDate);
                    twoDatesBetweenTime();
                }

            });
        }

    }

    public void twoDatesBetweenTime() {

        try {
            date_current = simpleDateFormat.parse(strDate);
        } catch (Exception e) {
            Log.i("LOG","Error parsing dateime at TimerService");
        }

        try {
            for(Bookmark bmark: mBookmarks) {
                if (bmark.getEndTime()==null){
                    continue;
                }
                if (date_current.getTime()>bmark.getEndTime().getTime()){
                    NotificationManager notificationManager = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(this, MainActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
                    Notification n  = new Notification.Builder(this)
                            .setContentTitle("Reminder! Time to check this out")
                            .setContentText(bmark.getTitle())
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true).build();
                    notificationManager.notify(0, n);
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
        Log.e("Service finish","Finish");
    }
}
