package com.cjlcboys.bookmarktracker.bookmarkrecyclerview;

import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Bookmark {
    private static final String JSON_OBJECT_UUID_HOLDER="JSON_OBJECT_UUID_HOLDER";
    private static final String JSON_OBJECT_LINK_HOLDER="JSON_OBJECT_LINK_HOLDER";
    private static final String JSON_OBJECT_DESCRIPTION_HOLDER="JSON_OBJECT_DESCRIPTION_HOLDER";
    private static final String JSON_OBJECT_TITLE_HOLDER="JSON_OBJECT_TITLE_HOLDER";
    private static final String JSON_OBJECT_REMINDER_HOLDER="JSON_OBJECT_REMINDER_HOLDER";

    private static final String JSON_OBJECT_UPLOADTIME_HOLDER="JSON_OBJECT_UPLOADTIME_HOLDER";
    private static final String JSON_OBJECT_ENDTIME_HOLDER="JSON_OBJECT_ENDTIME_HOLDER";

    private static final SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final Calendar calendar = Calendar.getInstance();

    public String id;

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public boolean reminder;
    private String title;
    private String url;
    private String desc;
    private Date uploadTime;
    private Date endTime; //how to implement?
//    private static int counter = 0;

    //public final int objectId;
//    private static Date getCurrentDate(){
//        return simpleDateFormat.format(calendar.getTime());
//    }

    public Bookmark(String title, String url, String desc,Date uptime,Date endtime,boolean reminder){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.url = url;
        this.desc = desc;
        this.reminder = reminder;
        try {
            this.uploadTime = simpleDateFormat.parse(simpleDateFormat.format(uptime));
            this.endTime = simpleDateFormat.parse(simpleDateFormat.format(endtime));
            Log.i("LOG","saved date"+this.endTime.toString());
        } catch (ParseException e) {
            Log.i("LOG",e.getMessage());
        }
    }
//
//    public Bookmark(String title, String url, String desc, Date uptime){
//        this.id = UUID.randomUUID().toString();
//        this.title = title;
//        this.url = url;
//        this.desc = desc;
//        try {
//            this.uploadTime = simpleDateFormat.parse(simpleDateFormat.format(uptime));
//        } catch (ParseException e) {
//            Log.i("LOG",e.getMessage());
//        }
//        this.endTime=null;
//    }

    public Bookmark(JSONObject job) throws JSONException, ParseException {
        if(job.has(JSON_OBJECT_UUID_HOLDER))
            this.id = job.getString(JSON_OBJECT_UUID_HOLDER);
        if(job.has(JSON_OBJECT_LINK_HOLDER))
            this.url = job.getString(JSON_OBJECT_LINK_HOLDER);
        if(job.has(JSON_OBJECT_TITLE_HOLDER))
            this.title = job.getString(JSON_OBJECT_TITLE_HOLDER);
        if(job.has(JSON_OBJECT_DESCRIPTION_HOLDER))
            this.desc = job.getString(JSON_OBJECT_DESCRIPTION_HOLDER);
        if(job.has(JSON_OBJECT_REMINDER_HOLDER))
            this.reminder = job.getBoolean(JSON_OBJECT_REMINDER_HOLDER);
        if(job.has(JSON_OBJECT_UPLOADTIME_HOLDER))
            this.uploadTime = simpleDateFormat.parse(job.getString(JSON_OBJECT_UPLOADTIME_HOLDER));
        if(job.has(JSON_OBJECT_ENDTIME_HOLDER)) {
            String tmp_date = job.getString(JSON_OBJECT_ENDTIME_HOLDER);
            this.endTime = simpleDateFormat.parse(tmp_date);
        }
    }

    //GETTERS
    public String getID() {
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }

    public String getUrl(){
        return this.url;
    }

    public String getDesc(){
        return this.desc;
    }

    public Date getUploadTime(){
        return this.uploadTime;
    }
    public Date getEndTime(){
        return this.endTime;
    }

    //SETTERS

    public void setTitle(String title){
        this.title = title;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }
    public void setEndTime(Date date){
        this.endTime = date;
    }

    public JSONObject getJSONObject() throws JSONException {
        JSONObject job = new JSONObject();
        job.put(JSON_OBJECT_UUID_HOLDER, this.id);
        job.put(JSON_OBJECT_LINK_HOLDER, this.url);
        job.put(JSON_OBJECT_TITLE_HOLDER, this.title);
        job.put(JSON_OBJECT_DESCRIPTION_HOLDER, this.desc);
        job.put(JSON_OBJECT_REMINDER_HOLDER, this.reminder);
        job.put(JSON_OBJECT_UPLOADTIME_HOLDER, simpleDateFormat.format(this.uploadTime));
        job.put(JSON_OBJECT_ENDTIME_HOLDER, simpleDateFormat.format(this.endTime));
        return job;
    }


    public boolean getReminder() {
        return this.reminder;
    }
}
