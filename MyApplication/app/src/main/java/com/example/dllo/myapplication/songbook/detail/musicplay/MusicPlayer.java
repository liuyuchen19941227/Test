package com.example.dllo.myapplication.songbook.detail.musicplay;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;


public class MusicPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {


    public static MediaPlayer player;
    public boolean isOver = false;



    // 初始化
    public MusicPlayer() {
        player = new MediaPlayer();
        // 设置音频流的类型
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//         当MediaPlayer调用prepare()方法时触发该监听器。
//        player.setOnPreparedListener(this);
        // 注册一个回调函数,在网络视频流缓冲变化时调用
//        player.setOnBufferingUpdateListener(this);
    }



    public void playMusic(String url) {
        try {
            player.reset();
            player.setDataSource(url);
//            player.prepareAsync();
            player.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
    }



    // 获取当前播放时间
    public void getMusicTime() {
    }


    // 获取歌曲总时长
    public int getTotalTime() {
        return player.getDuration();
    }





    // 判断是否是自然播放完成
    public boolean isThisMusicOver() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("MusicService", "播放完毕");
                isOver = true;
            }
        });
        return isOver;
    }




    // 判断是否正在播放
    public boolean isPlay() {
        if (player.isPlaying()) {
            return true;
        } else {
            return false;
        }
    }




    // 继续播放
    public void start() {
        player.start();
    }

    // 暂停
    public void pause(){
        player.pause();
    }







    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
    }



}
