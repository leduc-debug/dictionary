package com.lduwcs.dictionary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.dictionary.DefinitionActivity;
import com.lduwcs.dictionary.R;
import com.lduwcs.dictionary.database.DatabaseAccess2;
import com.lduwcs.dictionary.recycleview.RecyclerViewAdapter;
import com.lduwcs.dictionary.recycleview.Word;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class VietnameseToEnglish extends Fragment {


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
        DatabaseAccess2 databaseAccess2= DatabaseAccess2.getInstance(getContext(),"viet_anh");
        databaseAccess2.open();
        List<String> anhViet = databaseAccess2.getWords();
        String shortDefinition="";
        words = new ArrayList<>();
        for(String w : anhViet){
            String definition = databaseAccess2.getDefinition(w);
            Document document = Jsoup.parse(definition);
            Element elements = document.select("li").first();
            shortDefinition = elements.text();
            words.add(new Word(w,shortDefinition));
        }
        databaseAccess2.close();
        recyclerViewAdapter = new RecyclerViewAdapter(words,getActivity(),1);

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
                databaseAccess2.open();
                List<String> anhViet = databaseAccess2.getWords(s.toString());
                String shortDefinition="";
                words.clear();
                for(String w : anhViet){
                    String definition = databaseAccess2.getDefinition(w);
                    Document document = Jsoup.parse(definition);
                    Element elements = document.select("li").first();
                    shortDefinition = elements.text();
                    words.add(new Word(w,shortDefinition));
                }
                databaseAccess2.close();
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }
}