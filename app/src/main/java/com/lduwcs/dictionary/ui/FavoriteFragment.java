package com.lduwcs.dictionary.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lduwcs.dictionary.R;
import com.lduwcs.dictionary.database.DatabaseAccess;
import com.lduwcs.dictionary.recycleview.RecyclerViewAdapter;
import com.lduwcs.dictionary.recycleview.Word;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView rc_favorite;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Word> words;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);
        this.rc_favorite = (RecyclerView) root.findViewById(R.id.rc_favorite);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext(),"anh_viet");
        databaseAccess.open();
        ArrayList<String> ids =  databaseAccess.getFavoriteIds();
        words = new ArrayList<>();

        for(String id :ids){
            words.add(databaseAccess.getWord(id));
        }
        recyclerViewAdapter = new RecyclerViewAdapter(words,getContext(),0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rc_favorite.setLayoutManager(layoutManager);
        rc_favorite.setAdapter(recyclerViewAdapter);

        return root;
    }
}