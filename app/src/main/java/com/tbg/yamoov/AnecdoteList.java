package com.tbg.yamoov;

public class AnecdoteList {

    private String title;
    private String anec;
    private String date;

    public AnecdoteList(String title, String anec, String date) {
        this.title = title;
        this.anec = anec;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getAnec() {
        return anec;
    }

    public String getDate() {
        return date;
    }
}
