package com.example.dllo.myapplication.local_music;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseFragment;
import com.example.dllo.myapplication.detail.popup_window.MusicItemBean;
import com.example.dllo.myapplication.detail.popup_window.PopUpWindowRecyclerViewAdapter;

import java.util.ArrayList;

public class LocalMusicFragment extends BaseFragment{


    private RecyclerView rv;

    @Override
    protected int setLayout() {
        return R.layout.fragmemt_localmusic;
    }

    @Override
    protected void initView() {

        rv = bindView(R.id.rv_localmusic);

    }

    @Override
    protected void initData() {
        ArrayList<MusicItemBean> musicItemBeen = new ArrayList<>();


        // 查询本地音乐 数据库写完之后要封装一下
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if(cursor != null) {
            while(cursor.moveToNext()){
                // 歌曲名称
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                // 歌曲作者
                String author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.EXTRA_MEDIA_ARTIST));
                MusicItemBean musicItemBean = new MusicItemBean(title, author, 0);
                musicItemBeen.add(musicItemBean);
            }
        }

        Log.d("LocalMusicFragment", "musicItemBeen.size():" + musicItemBeen.size());




        PopUpWindowRecyclerViewAdapter adapter = new PopUpWindowRecyclerViewAdapter(getContext());
        adapter.setMusicItemBeen(musicItemBeen);
        rv.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);



    }







}
