package com.cjlcboys.bookmarktracker.bookmarkrecyclerview;

import java.util.Date;

public class Bookmark {
    public final int id;
    private String title;
    private String url;
    private String desc;
    private Date uploadTime;
    private Date timeRemaining; //how to implement?
    private static int counter = 0;

    //public final int objectId;


    public Bookmark(String title, String url, String desc){
        this.id = counter++;
        this.title = title;
        this.url = url;
        this.desc = desc;
    }

    public Bookmark(String title, String url){
        this.id = counter++;
        this.title = title;
        this.url = url;
        this.desc = "";
    }

    //GETTERS

    public String getTitle(){
        return this.title;
    }

    public String getUrl(){
        return this.url;
    }

    public String getDesc(){
        return this.desc;
    }

    public String getUploadTime(){
        return this.uploadTime.toString();
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


}
