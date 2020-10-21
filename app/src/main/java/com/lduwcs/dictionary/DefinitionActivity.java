package com.lduwcs.dictionary;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.lduwcs.dictionary.database.DatabaseAccess;
import com.lduwcs.dictionary.database.DatabaseAccess2;

public class DefinitionActivity extends AppCompatActivity {

    private WebView wvDefinition;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);
        wvDefinition = findViewById(R.id.tv_definition);
        actionBar = getSupportActionBar();

        String style = " <style> body>span {color: #f81111; font-size: 1.2em;}" +
                                "body>ul>li {color: #8215ad;}" +
                                "li>span {color: #1a1a1a;}" +
                        "</style>";

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
            wvDefinition.loadDataWithBaseURL(null,style+definition,"text/html", "utf-8", null);
        }
        else {
            DatabaseAccess2 dbAccess = DatabaseAccess2.getInstance(this,"viet_anh");
            dbAccess.open();
            String definition = dbAccess.getDefinition(word);
            dbAccess.close();
            //Hiển thị trên textView
            wvDefinition.loadDataWithBaseURL(null,style+definition,"text/html", "utf-8", null);
        }

    }
    public void setTitle(String title){
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(25);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(textView);
    }
}