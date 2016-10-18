package com.example.dllo.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.dllo.myapplication.detail.musicplay.MusicBean;

public class DBTools {


    private SQLiteDatabase database;


    public SQLiteDatabase getDatabase() {
        return database;
    }

    public DBTools(Context context) {
        MySQLHelper mySQLHelper = new MySQLHelper(context, DBValues.SQL_NAME, null, 1);
        database = mySQLHelper.getWritableDatabase();
    }


    public void insertDB(MusicBean musicBean) {
        ContentValues values = new ContentValues();
//        values.put(musicBean.getSonginfo());

        database.insert(DBValues.TABLE_NAME_MUSIC, null, values);

    }















}
