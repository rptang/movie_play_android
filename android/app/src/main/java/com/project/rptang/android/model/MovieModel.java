package com.project.rptang.android.model;

import java.io.Serializable;

public class MovieModel implements Serializable {

    private String url;
    private String title;
    private String time;
    private int position = 0;

    public MovieModel() {}

    public MovieModel(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public MovieModel(String url, String title, String time) {
        this.url = url;
        this.title = title;
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", position=" + position +
                '}';
    }
}
