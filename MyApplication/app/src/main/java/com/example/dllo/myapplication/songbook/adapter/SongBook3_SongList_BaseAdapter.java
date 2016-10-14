package com.example.dllo.myapplication.songbook.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.myapplication.songbook.bean.SongBook3_ListBean;
import com.example.dllo.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongBook3_SongList_BaseAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<SongBook3_ListBean> beans;


    public SongBook3_SongList_BaseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBeans(ArrayList<SongBook3_ListBean> beans) {
        this.beans = beans;
    }


    @Override
    public int getCount() {
        return beans.get(0).getDiyInfo().size();
    }

    @Override
    public Object getItem(int i) {
        return beans.get(0).getDiyInfo().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_songbook3_musiclist, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            Log.d("SongBook3_SongList_Base", "233");
        } else {
            viewHolder = (ViewHolder) view.getTag();
            Log.d("SongBook3_SongList_Base", "???");
        }


        viewHolder.title.setText(beans.get(0).getDiyInfo().get(i).getTitle());
        viewHolder.author.setText(beans.get(0).getDiyInfo().get(i).getUsername());
        Picasso.with(mContext).load(beans.get(0).getDiyInfo().get(i).getList_pic()).into(viewHolder.img);

        return view;
    }



    private class ViewHolder{

        private ImageView img;
        private TextView title;
        private TextView author;

        public ViewHolder(View view) {
            img = (ImageView) view.findViewById(R.id.iv_songbook1_recommend3_music1);
            title = (TextView) view.findViewById(R.id.tv_songbook1_recommend3_title1);
            author = (TextView) view.findViewById(R.id.tv_songbook1_recommend3_author1);
        }
    }




}
