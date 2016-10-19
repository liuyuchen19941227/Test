package com.example.dllo.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLHelper extends SQLiteOpenHelper{



    public MySQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 数据库的构造
        sqLiteDatabase.execSQL("create table " +DBValues.TABLE_NAME_MUSIC+ "(id integer primary key autoincrement ,"+ DBValues.MUSIC_SONG_ID+" text ,"+DBValues.MUSIC_NAME+" text ,"+DBValues.MUSIC_AUTHOR+" text ,"+DBValues.MUSIC_File_LINK+" text ,"+DBValues.MUSIC_Img_Link+" text ,"+DBValues.MUSIC_LYRIC_LINK+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
