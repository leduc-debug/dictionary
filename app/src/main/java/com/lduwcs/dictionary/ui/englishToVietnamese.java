package com.lduwcs.dictionary.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.dictionary.R;
import com.lduwcs.dictionary.database.DatabaseAccess;
import com.lduwcs.dictionary.recycleview.RecyclerViewAdapter;
import com.lduwcs.dictionary.recycleview.Word;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class englishToVietnamese extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private EditText ed_search;
    private ArrayList<Word> words;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_english_to_vietnamese, container, false);

        this.ed_search = (EditText) root.findViewById(R.id.ed_search);
        this.recyclerView = (RecyclerView) root.findViewById(R.id.rc_etov);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext(),"anh_viet");
        databaseAccess.open();
        List<String> anhViet = databaseAccess.getWords();
        String shortDefinition="";
        words = new ArrayList<>();
        for(String w : anhViet){
            String definition = databaseAccess.getDefinition(w);
            Document document = Jsoup.parse(definition);
            Element elements = document.select("li").first();
            shortDefinition = elements.text();
            words.add(new Word(w,shortDefinition));
        }
        databaseAccess.close();
        recyclerViewAdapter = new RecyclerViewAdapter(words,getActivity(),0);

        //LinearLayout settting
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        //Add listener
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                databaseAccess.open();
                List<String> anhViet = databaseAccess.getWords(s.toString());
                String shortDefinition="";
                words.clear();
                for(String w : anhViet){
                    String definition = databaseAccess.getDefinition(w);
                    Document document = Jsoup.parse(definition);
                    Element elements = document.select("li").first();
                    shortDefinition = elements.text();
                    words.add(new Word(w,shortDefinition));
                }
                databaseAccess.close();
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}