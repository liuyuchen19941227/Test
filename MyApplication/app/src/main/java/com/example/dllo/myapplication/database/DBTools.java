package com.example.dllo.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.dllo.myapplication.detail.music_content.DownloadMusicBean;
import com.example.dllo.myapplication.detail.musicplay.MusicBean;

import java.util.ArrayList;


public class DBTools {


    private SQLiteDatabase database;


    public SQLiteDatabase getDatabase() {
        return database;
    }

    public DBTools(Context context) {
        MySQLHelper mySQLHelper = new MySQLHelper(context, DBValues.SQL_NAME, null, 1);
        database = mySQLHelper.getWritableDatabase();
    }


    // 本地存储
    public void insertDB(MusicBean musicBean) {
        ContentValues values = new ContentValues();
        values.put(DBValues.MUSIC_SONG_ID, musicBean.getSonginfo().getSong_id());
        values.put(DBValues.MUSIC_NAME, musicBean.getSonginfo().getTitle());
        values.put(DBValues.MUSIC_AUTHOR, musicBean.getSonginfo().getAuthor());
        values.put(DBValues.MUSIC_File_LINK, musicBean.getBitrate().getFile_link());
        values.put(DBValues.MUSIC_Img_Link, musicBean.getSonginfo().getPic_big());
        values.put(DBValues.MUSIC_LYRIC_LINK, musicBean.getSonginfo().getLrclink());
        database.insert(DBValues.TABLE_NAME_MUSIC, null, values);
        Log.d("DBTools", "本地存储完成");
    }



    public boolean queryDB(String songId) {
        Log.d("DBTools2", songId);
        Cursor cursor = database.query(DBValues.TABLE_NAME_MUSIC, null, DBValues.MUSIC_SONG_ID + " = ?", new String[] {songId}, null, null, null);
        // cursor已经创建出来了,所以一定不为空
        if (cursor.getCount() > 0) {
            Log.d("DBTools", "存在");
            return true;
        } else {
            Log.d("DBTools", "不存在");
            return false;
        }
    }




    public ArrayList<DownloadMusicBean> queryAllDB(Context context) {
        Cursor cursor = database.query(DBValues.TABLE_NAME_MUSIC, null, null, null, null, null, null);
        DownloadMusicBean downloadMusicBean = new DownloadMusicBean();
        ArrayList<DownloadMusicBean> musicBeanList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                downloadMusicBean.setSongId(cursor.getString(cursor.getColumnIndex(DBValues.MUSIC_SONG_ID)));
                downloadMusicBean.setSong(cursor.getString(cursor.getColumnIndex(DBValues.MUSIC_NAME)));
                downloadMusicBean.setAuthor(cursor.getString(cursor.getColumnIndex(DBValues.MUSIC_AUTHOR)));
                downloadMusicBean.setMusicUrl(cursor.getString(cursor.getColumnIndex(DBValues.MUSIC_File_LINK)));
                downloadMusicBean.setImgUrl(cursor.getString(cursor.getColumnIndex(DBValues.MUSIC_Img_Link)));
                downloadMusicBean.setLrcLink(cursor.getString(cursor.getColumnIndex(DBValues.MUSIC_LYRIC_LINK)));
                musicBeanList.add(downloadMusicBean);
            }
        }
        return musicBeanList;
    }















}
