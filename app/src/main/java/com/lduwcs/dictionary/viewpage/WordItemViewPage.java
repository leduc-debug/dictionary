package com.lduwcs.dictionary.viewpage;

public class WordItemViewPage {
    private String word;
    private String answer;
    private int score;
    public boolean isDone=false;

    public WordItemViewPage(String word, String answer, int score) {
        this.word = word;
        this.answer = answer;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAnswer() {
        return "Your answer: "+answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getScore() {
        return "Score: "+score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
