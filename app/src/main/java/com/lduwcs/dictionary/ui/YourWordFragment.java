package com.lduwcs.dictionary.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lduwcs.dictionary.R;
import com.lduwcs.dictionary.database.DatabaseAccess;
import com.lduwcs.dictionary.viewpage.SliderAdapter;
import com.lduwcs.dictionary.viewpage.WordItemViewPage;

import java.util.ArrayList;
import java.util.Locale;


public class YourWordFragment extends Fragment {

    private Context mContext;
    private ViewPager2 viewPager2;
    private SliderAdapter sliderAdapter;
    private static final int REQ_CODE_SPEECH_INPUT =1030;
    private int position;
    ArrayList<WordItemViewPage> words = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_your_word, container, false);
        viewPager2 = (ViewPager2) root.findViewById(R.id.vp_yourWord);


        words.add(new WordItemViewPage(randomWord(),"",0));

        sliderAdapter = new SliderAdapter(words,getContext(),this);
        viewPager2.setAdapter(sliderAdapter);

        viewPager2.setOffscreenPageLimit(3);

        return root;
    }
    public void promptSpeechInput() {
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

                    sliderAdapter.showAnswer(viewPager2.getCurrentItem(),result.get(0));
                }
                break;
            }

        }
    }

    public void initNewWordTest(){
        words.add(new WordItemViewPage(randomWord(),"",0));
        sliderAdapter.notifyDataSetChanged();
    }

    private String randomWord(){
        int id =(int)((Math.random()+1)*2000);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getContext(),"anh_viet");
        databaseAccess.open();
        String s = databaseAccess.getWord(id+"").getTitle();
        return s;
    }

}