package com.example.dllo.myapplication.songbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.songbook.bean.SongBook4_VedioBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongBook4_VedioList_BaseAdapter extends BaseAdapter{

    private ArrayList<SongBook4_VedioBean> arrayList;
    private Context mContext;

    public SongBook4_VedioList_BaseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(ArrayList<SongBook4_VedioBean> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.get(0).getResult().getMv_list().size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(0).getResult().getMv_list().get(i);
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
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvSong.setText(arrayList.get(0).getResult().getMv_list().get(i).getTitle());
        viewHolder.tvAuthor.setText(arrayList.get(0).getResult().getMv_list().get(i).getArtist());

        Picasso.with(mContext).load(arrayList.get(0).getResult().getMv_list().get(i).getThumbnail2()).into(viewHolder.iv);

        return view;
    }



    private class ViewHolder {

        private ImageView iv;
        private TextView tvSong;
        private TextView tvAuthor;

        private ViewHolder(View view){

            iv = (ImageView) view.findViewById(R.id.iv_songbook1_recommend3_music1);
            tvSong = (TextView) view.findViewById(R.id.tv_songbook1_recommend3_title1);
            tvAuthor = (TextView) view.findViewById(R.id.tv_songbook1_recommend3_author1);

        }



    }




}
