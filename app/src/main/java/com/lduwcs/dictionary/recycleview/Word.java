package com.lduwcs.dictionary.recycleview;

public class Word {
    private String title;
    private String short_definition;

    public Word(String title, String short_definition) {
        this.title = title;
        this.short_definition = short_definition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShort_definition() {
        return short_definition;
    }

    public void setShort_definition(String short_definition) {
        this.short_definition = short_definition;
    }
}
