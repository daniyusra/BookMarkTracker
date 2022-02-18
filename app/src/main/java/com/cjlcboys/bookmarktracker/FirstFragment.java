package com.cjlcboys.bookmarktracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjlcboys.bookmarktracker.bookmarkrecyclerview.Bookmark;
import com.cjlcboys.bookmarktracker.bookmarkrecyclerview.BookmarksAdapter;
import com.cjlcboys.bookmarktracker.databinding.FragmentFirstBinding;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        RecyclerView rvBookmarks = (RecyclerView) binding.rvBookmarks;

        List<Bookmark> bookmarks = new ArrayList<>();

        //bookmarks.add(new Bookmark("BRUH","BRUH.COM","BRUHHHH"));
        //bookmarks.add(new Bookmark("BRUH2","BRUH.COM","BRUHHHH"));

        BookmarksAdapter adapter = new BookmarksAdapter(bookmarks);
        // Attach the adapter to the recyclerview to populate items
        rvBookmarks.setAdapter(adapter);
        // Set layout manager to position the items
        rvBookmarks.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}