package com.example.dllo.myapplication.detail.music_content.lyric;

/**
 * 歌词拖动时候的监听类
 */
public interface ILrcViewListener {
    /**
     * 当歌词被用户上下拖动的时候回调该方法
     */
    void onLrcSeeked(int newPosition, LrcRow row);
}
