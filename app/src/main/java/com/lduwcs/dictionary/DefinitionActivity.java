package com.lduwcs.dictionary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lduwcs.dictionary.database.DatabaseAccess;
import com.lduwcs.dictionary.database.DatabaseAccess2;

import java.util.Locale;

import static android.content.res.Configuration.UI_MODE_NIGHT_MASK;

public class DefinitionActivity extends AppCompatActivity {

    private WebView wvDefinition;
    private ActionBar actionBar;
    private FloatingActionButton btn_speak;
    private TextToSpeech mTTS;
    private String style;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);
        wvDefinition = findViewById(R.id.tv_definition);
        actionBar = getSupportActionBar();
        btn_speak = findViewById(R.id.btn_speak);

        getSystemTheme();

        //Lấy dữ liệu form kia gởi qua
        Intent intent = getIntent();
        String word = intent.getStringExtra("WORD");
        int type = intent.getIntExtra("TYPE",0);

        setTitle(word);

        if(type==0) {
            DatabaseAccess dbAccess = DatabaseAccess.getInstance(this,"anh_viet");
            dbAccess.open();
            String definition = dbAccess.getDefinition(word);
            dbAccess.close();
            //Hiển thị trên textView
            setTextViewHTML(style+definition);
//            wvDefinition.loadDataWithBaseURL(null,style+definition,"text/html", "utf-8", null);
        }
        else {
            DatabaseAccess2 dbAccess = DatabaseAccess2.getInstance(this,"viet_anh");
            dbAccess.open();
            String definition = dbAccess.getDefinition(word);
            dbAccess.close();
            //Hiển thị trên textView
            setTextViewHTML(style+definition);
          //  wvDefinition.loadDataWithBaseURL(null,style+definition,"text/html", "utf-8", null);
        }

        mTTS = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    int result = mTTS.setLanguage(Locale.UK);
                    if(result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.d("TAG", "onInit: "+ "lang not support" );
                    }else{
                        btn_speak.setEnabled(true);
                    }
                }else{
                    Log.d("TAG", "onInit: "+ "dmdmdmdmd" );
                }
            }
        });


        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(word);
            }
        });

    }

    private void speak(String text){
        mTTS.setPitch(1);
        mTTS.setSpeechRate(1);
        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null);
    }

    @Override
    protected void onDestroy() {
        if(mTTS != null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }


    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                Toast.makeText(getBaseContext(),"hehe boi",Toast.LENGTH_LONG).show();
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        wvDefinition.loadDataWithBaseURL(null,html,"text/html", "utf-8", null);
    }


    public void setTitle(String title){
        TextView tv = (TextView) findViewById(R.id.tv_title_appbar);
        tv.setText(title);
        ((Button)findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void getSystemTheme(){
        int theme = getResources().getConfiguration().uiMode & UI_MODE_NIGHT_MASK;
        switch (theme){
            case Configuration.UI_MODE_NIGHT_NO:
                wvDefinition.setBackgroundColor(getResources().getColor(R.color.white));
                style = " <style> .title {color: #2c9722;font-size: 1.3em;}"+
                        "body>span {color: #f81111; font-size: 1.2em;}" +
                        "body>ul>li {color: #8215ad;}" +
                        "li>span {color: #1a1a1a;}" +
                        "</style>";
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                wvDefinition.setBackgroundColor(getResources().getColor(R.color.black));
                style = " <style> .title {color: #2c9722;font-size: 1.3em;}"+
                        "body>span {color: #f81111; font-size: 1.2em;}" +
                        "body>ul>li {color: #8215ad;}" +
                        "li>span {color: #dbdbdb;}" +
                        "</style>";
                break;
        }
    }
}