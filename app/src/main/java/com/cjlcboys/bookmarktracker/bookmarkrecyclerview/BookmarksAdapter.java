package com.cjlcboys.bookmarktracker.bookmarkrecyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cjlcboys.bookmarktracker.R;

import java.util.List;

public class BookmarksAdapter  extends
        RecyclerView.Adapter<BookmarksAdapter.ViewHolder>{

    private static ClickListener clickListener;

    public void setOnClickListener(ClickListener clickListener) {
        BookmarksAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onItemClick(Bookmark state, View v);
        void onItemLongClick(Bookmark state, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView bookmarkTextView;
        public TextView bookmarkUrlView;
        public Button editButton;
        private Bookmark mState;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            bookmarkTextView= (TextView) itemView.findViewById(R.id.bookmark_title);
            bookmarkUrlView = (TextView) itemView.findViewById(R.id.bookmark_url);
            editButton = (Button) itemView.findViewById(R.id.edit_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BookmarksAdapter.clickListener.onItemClick(mState, v);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BookmarksAdapter.clickListener.onItemLongClick(mState, v);
                    return false;
                }
            });
        }

        public void setState(Bookmark bookmark) {
            this.mState = bookmark;
        }
    }


    // Store a member variable for the contacts
    private List<Bookmark> mBookmarks;

    // Pass in the contact array into the constructor
    public BookmarksAdapter(List<Bookmark> bookmarks) {
        mBookmarks = bookmarks;
    }

    @NonNull
    @Override
    public BookmarksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.bookmark_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Bookmark bookmark = mBookmarks.get(position);

        // Set item views based on your views and data model
        TextView textView1 = holder.bookmarkTextView;
        textView1.setText(bookmark.getTitle());
        TextView textView2 = holder.bookmarkUrlView;
        textView2.setText(bookmark.getUrl());
        Button button = holder.editButton;
        holder.setState(bookmark);

        //button.setText(contact.isOnline() ? "Message" : "Offline");
        //button.setEnabled(contact.isOnline());

    }
    @Override
    public int getItemCount() {
        return mBookmarks.size();
    }
}
