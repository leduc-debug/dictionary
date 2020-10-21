package com.lduwcs.dictionary.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lduwcs.dictionary.R;
import com.lduwcs.dictionary.database.DatabaseAccess;
import com.lduwcs.dictionary.recycleview.RecyclerViewAdapter;
import com.lduwcs.dictionary.recycleview.Word;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class englishToVietnamese extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private EditText ed_search;
    private ArrayList<Word> words;
    private ImageButton btnVoice;
    private static final int REQ_CODE_SPEECH_INPUT = 1010;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "onCreate: "+"fghgbjjn");
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
        this.btnVoice = (ImageButton) root.findViewById(R.id.img_btn_voice);
        this.btnVoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


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

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == -1 && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    ed_search.setText(result.get(0));
                }
                break;
            }

        }
    }
}