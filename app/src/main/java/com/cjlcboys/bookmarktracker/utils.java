package com.cjlcboys.bookmarktracker;

import android.util.Log;
import com.cjlcboys.bookmarktracker.bookmarkrecyclerview.Bookmark;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

class Helper{
    public static final String BOOKMARKS_FILE_NAME="com.cjlcboys.bookmarktracker.bookmarks.json";

    public static void load_bookmarks(List<Bookmark> bookmarks, File file) throws IOException, JSONException, ParseException {
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();
        String content = stringBuilder.toString();
        JSONArray bkmrksarray = new JSONArray(content);
        for(int i=0;i<bkmrksarray.length();i++){
            bookmarks.add(new Bookmark(bkmrksarray.getJSONObject(i)));
        }
    }

    public static void save_bookmarks(List<Bookmark> bookmarks, File file) throws IOException, JSONException{
        if(bookmarks.size()==0){
            Log.i("log","No bookmarks found. Skipping");
        }
        JSONArray jarr = new JSONArray();
        for(Bookmark bmark: bookmarks){
            jarr.put(bmark.getJSONObject());
        }
        FileWriter writer = new FileWriter(file,false);
        writer.write(jarr.toString());
        writer.flush();
        writer.close();
    }
}