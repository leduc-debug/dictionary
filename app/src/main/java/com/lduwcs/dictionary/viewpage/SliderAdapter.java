package com.lduwcs.dictionary.viewpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.dictionary.R;
import com.lduwcs.dictionary.database.DatabaseAccess;
import com.lduwcs.dictionary.ui.YourWordFragment;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderAdapterViewHolder> {

    private ArrayList<WordItemViewPage> words ;
    private Context mContext;
    private YourWordFragment yourWordFragment;


    public SliderAdapter(ArrayList<WordItemViewPage> words, Context mContext,YourWordFragment yourWordFragment) {
        this.words = words;
        this.mContext = mContext;
        this.yourWordFragment = yourWordFragment;
    }

    public void showAnswer(int position,String yAnswer){
        words.get(position).setAnswer(yAnswer);
        words.get(position).setScore(getScore(words.get(position).getWord(),yAnswer));
        words.get(position).isDone=true;
        if(position==words.size()-1)
            yourWordFragment.initNewWordTest();
        notifyItemChanged(position);
    }

    private int getScore(String s1, String s2){
        if(s1.equals(s2)) return 10;
        return (int)((Math.random())*6);
    }

    @NonNull
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.word_swipe_card_view, parent, false);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapterViewHolder holder, int position) {
//        Toast.makeText(mContext,"hahah",Toast.LENGTH_LONG).show();
        holder.tv_word.setText(words.get(position).getWord());
        holder.tv_answer.setText(words.get(position).getAnswer());
        holder.tv_score.setText(words.get(position).getScore()+"");
        holder.img_pronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yourWordFragment.promptSpeechInput();
            }
        });
    }

    @Override
    public int getItemCount() {
        return words.size();
    }



    public class SliderAdapterViewHolder  extends  RecyclerView.ViewHolder{

        public TextView tv_word;
        public TextView tv_answer;
        public TextView tv_score;
        public ImageView img_pronunciation;

        public SliderAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_word = (TextView) itemView.findViewById(R.id.tv_word);
            tv_answer = (TextView) itemView.findViewById(R.id.tv_answer);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            img_pronunciation = (ImageView) itemView.findViewById(R.id.img_pronunciation);
        }
    }

}
