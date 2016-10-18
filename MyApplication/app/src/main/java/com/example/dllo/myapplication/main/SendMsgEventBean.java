package com.example.dllo.myapplication.main;

import com.example.dllo.myapplication.detail.musicplay.MusicBean;
import com.example.dllo.myapplication.detail.popup_window.MusicItemBean;

import java.util.ArrayList;

public class SendMsgEventBean {

    private String author;
    private String song;
    private String img;
    private String lrclink;
    private String url;
    // 整个歌单的歌曲
    private ArrayList<String> musics;
    private ArrayList<MusicItemBean> musicItemBeanArrayList;
    private MusicBean musicBean;


    public MusicBean getMusicBean() {
        return musicBean;
    }

    public void setMusicBean(MusicBean musicBean) {
        this.musicBean = musicBean;
    }

    public ArrayList<MusicItemBean> getMusicItemBeanArrayList() {
        return musicItemBeanArrayList;
    }

    public void setMusicItemBeanArrayList(ArrayList<MusicItemBean> musicItemBeanArrayList) {
        this.musicItemBeanArrayList = musicItemBeanArrayList;
    }



    public String getLrclink() {
        return lrclink;
    }

    public void setLrclink(String lrclink) {
        this.lrclink = lrclink;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setMusics(ArrayList<String> musics) {
        this.musics = musics;
    }

    public ArrayList<String> getMusics() {
        return musics;
    }
}
