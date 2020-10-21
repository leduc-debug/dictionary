package com.lduwcs.dictionary.recycleview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.dictionary.DefinitionActivity;
import com.lduwcs.dictionary.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{

    private ArrayList<Word> words ;
    private Context mContext;
    private int type;

    public RecyclerViewAdapter(ArrayList<Word> words, Context context,int type) {
        this.words=words;
        this.mContext = context;
        this.type=type;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.word_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.tv_word.setText(words.get(position).getTitle());
        holder.tv_definition.setText(words.get(position).getShort_definition());
        holder.ll_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, com.lduwcs.dictionary.DefinitionActivity.class);

                intent.putExtra("WORD",words.get(position).getTitle());
                intent.putExtra("TYPE",type);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_word;
        public TextView tv_definition;
        public LinearLayout ll_word;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_word = (TextView) itemView.findViewById(R.id.tv_word);
            tv_definition = (TextView) itemView.findViewById(R.id.tv_short_definition);
            ll_word = (LinearLayout) itemView.findViewById(R.id.ll_word);
        }
    }
}
