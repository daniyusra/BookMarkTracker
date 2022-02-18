package com.cjlcboys.bookmarktracker;

import android.content.Intent;
import android.os.Bundle;

import com.cjlcboys.bookmarktracker.bookmarkrecyclerview.Bookmark;
import com.cjlcboys.bookmarktracker.bookmarkrecyclerview.BookmarksAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjlcboys.bookmarktracker.databinding.ActivityMainBinding;
import com.cjlcboys.bookmarktracker.Helper;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private List<Bookmark> bookmarks;
    private RecyclerView rvBookmarks;
    public BookmarksAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                createDialog();
            }


        });

        Intent intent = getIntent();

        if (intent.getExtras() != null){
            if (intent.getType().equals("text/plain")) {
            //start fragment here
                createDialog("",intent.getStringExtra(Intent.EXTRA_TEXT));
            }
        }

        //if (intent.getType().equals("text/plain")) {
            //start fragment here
        //}

        rvBookmarks = (RecyclerView) binding.rvBookmarks;

        bookmarks = new ArrayList<>();

        adapter = new BookmarksAdapter(bookmarks);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvBookmarks);
        //Checking the availability state of the External Storage.
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("LOG","External Storage is loaded");
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS), Helper.BOOKMARKS_FILE_NAME);
            try {
                Helper.load_bookmarks(bookmarks,file);
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.i("LOG","No file found/Error parsing file. Bookmarks will not be loaded");
            }
        }
        else{
            Log.i("LOG","External Storage is not loaded. Bookmarks will not be loaded");
        }
        rvBookmarks.setAdapter(adapter);
        rvBookmarks.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("LOG","External Storage is loaded");
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS), Helper.BOOKMARKS_FILE_NAME);
            try {
                Helper.save_bookmarks(bookmarks,file);
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.i("LOG","No file found/Error parsing file. Bookmarks will not be saved");
            }
        }
        else{
            Log.i("LOG","External Storage is not loaded. Bookmarks will not be saved");
        }
    }

    public void createDialog(){
        createDialog("","");
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            bookmarks.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    };


    public void createDialog(String title, String url){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        final View makeNewBookmarkView  = getLayoutInflater().inflate(R.layout.make_bookmark,null);
        //bind to individual GUI objects
        dialogBuilder.setView(makeNewBookmarkView);
        AlertDialog dialog = dialogBuilder.create();
        EditText newbookmarktitle = (EditText) makeNewBookmarkView.findViewById(R.id.edit_text_title);
        EditText newbookmarkurl = (EditText) makeNewBookmarkView.findViewById(R.id.edit_text_url);
        EditText newbookmarkdesc = (EditText) makeNewBookmarkView.findViewById(R.id.edit_text_description);
        Button newbookmarkbutton = (Button) makeNewBookmarkView.findViewById(R.id.button_create_bookmark);
        Button newbookmarkcancel = (Button)  makeNewBookmarkView.findViewById(R.id.button_exit);
        ImageButton setdatetime = (ImageButton) makeNewBookmarkView.findViewById(R.id.imageButton2);
        TextView newdatetime = (TextView) makeNewBookmarkView.findViewById(R.id.textViewDate);
        CheckBox checkboxreminder = (CheckBox) makeNewBookmarkView.findViewById(R.id.CheckBoxReminder);

        newdatetime.setText(Calendar.getInstance().getTime().toString());

        newbookmarktitle.setText(title);
        newbookmarkurl.setText(url);
        dialog.show();




        newbookmarkbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();

                if (checkboxreminder.isChecked()){
                    bookmarks.add(new Bookmark(newbookmarktitle.getText().toString(),newbookmarkurl.getText().toString(),newbookmarkdesc.getText().toString(),Calendar.getInstance().getTime(), new Date((String) newdatetime.getText())));
                } else {
                    bookmarks.add(new Bookmark(newbookmarktitle.getText().toString(),newbookmarkurl.getText().toString(),newbookmarkdesc.getText().toString(),Calendar.getInstance().getTime(),new Date()));
                }



                dialog.dismiss();
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    Log.i("LOG","External Storage is loaded");
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS), Helper.BOOKMARKS_FILE_NAME);
                    try {
                        Helper.save_bookmarks(bookmarks,file);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Log.i("LOG","No file found/Error parsing file. Bookmarks will not be saved");
                    }
                }
                else{
                    Log.i("LOG","External Storage is not loaded. Bookmarks will not be saved");
                }
            }
        });

        setdatetime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(MainActivity.this, R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());

                        newdatetime.setText(calendar.getTime().toString());
                        alertDialog.dismiss();
                    }});
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        newbookmarkcancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();
                //bookmarks.add(new Bookmark(newbookmarktitle.getText().toString(),newbookmarkurl.getText().toString(),newbookmarkdesc.getText().toString()));
                dialog.dismiss();
            }
        });
    }
}