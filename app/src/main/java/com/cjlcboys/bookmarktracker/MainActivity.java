package com.cjlcboys.bookmarktracker;

import android.content.Intent;
import android.os.Bundle;

import com.cjlcboys.bookmarktracker.bookmarkrecyclerview.Bookmark;
import com.cjlcboys.bookmarktracker.bookmarkrecyclerview.BookmarksAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjlcboys.bookmarktracker.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private List<Bookmark> bookmarks;
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

            public void createDialog(){

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
                dialog.show();

                newbookmarkbutton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //      .setAction("Action", null).show();
                        bookmarks.add(new Bookmark(newbookmarktitle.getText().toString(),newbookmarkurl.getText().toString(),newbookmarkdesc.getText().toString()));
                        dialog.dismiss();
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
        });

        Intent intent = getIntent();

        //if (intent.getType().equals("text/plain")) {
            //start fragment here
        //}

        RecyclerView rvBookmarks = (RecyclerView) binding.rvBookmarks;

        bookmarks = new ArrayList<>();

        bookmarks.add(new Bookmark("BRUH","BRUH.COM","BRUHHHH"));
        bookmarks.add(new Bookmark("BRUH2","BRUH.COM","BRUHHHH"));

        BookmarksAdapter adapter = new BookmarksAdapter(bookmarks);
        // Attach the adapter to the recyclerview to populate items
        rvBookmarks.setAdapter(adapter);
        // Set layout manager to position the items
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
}