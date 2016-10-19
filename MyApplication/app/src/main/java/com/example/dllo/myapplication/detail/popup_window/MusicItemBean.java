package com.example.dllo.myapplication.detail.popup_window;

public class MusicItemBean {

    private String song;
    private String author;
    private int isPlay;
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public MusicItemBean() {
    }

    public MusicItemBean(String song, String author, int isPlay) {
        this.song = song;
        this.author = author;
        this.isPlay = isPlay;
    }

    public int getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }


    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }




}
