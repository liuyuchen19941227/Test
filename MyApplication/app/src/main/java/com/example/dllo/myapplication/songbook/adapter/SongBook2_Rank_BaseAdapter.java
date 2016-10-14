package com.example.dllo.myapplication.songbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.dllo.myapplication.songbook.bean.SongBook2_RankBean;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.cache.DoubleCache;

import java.util.ArrayList;

public class SongBook2_Rank_BaseAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<SongBook2_RankBean> arrayList;
    private RequestQueue queue;
    private ImageLoader imageLoader;


    public SongBook2_Rank_BaseAdapter(Context mContext) {
        this.mContext = mContext;
        queue = Volley.newRequestQueue(mContext);
        imageLoader = new ImageLoader(queue, new DoubleCache(mContext));


    }

    public void setArrayList(ArrayList<SongBook2_RankBean> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        // 注意size
        return arrayList == null ? 0 : arrayList.get(0).getContent().size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(0).getContent().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_songbook2_rank, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 直接传类
        // 用arrayList户更好进行数据操作
        viewHolder.title.setText(arrayList.get(0).getContent().get(i).getName());
        viewHolder.top1.setText(arrayList.get(0).getContent().get(i).getContent2().get(0).getTitle()
                + " - " + arrayList.get(0).getContent().get(i).getContent2().get(0).getAuthor());
        viewHolder.top2.setText(arrayList.get(0).getContent().get(i).getContent2().get(1).getTitle()
                + " - " + arrayList.get(0).getContent().get(i).getContent2().get(1).getAuthor());
        viewHolder.top3.setText(arrayList.get(0).getContent().get(i).getContent2().get(2).getTitle()
                + " - " + arrayList.get(0).getContent().get(i).getContent2().get(2).getAuthor());

        imageLoader.get(arrayList.get(0).getContent().get(i).getPic_s192().toString(), ImageLoader.getImageListener(viewHolder.img, R.mipmap.ic_launcher, R.mipmap.ic_launcher));


//        Picasso.with(mContext).load(arrayList.get(0).getContent().get(i).getPic_s192().toString()).into(viewHolder.img);

        return view;
    }


    // 减少findViewById的操作
    public class ViewHolder {

        private ImageView img;
        private TextView title;
        private TextView top1;
        private TextView top2;
        private TextView top3;

        public ViewHolder(View view) {
            img = (ImageView) view.findViewById(R.id.iv_songbook2_rank);
            title = (TextView) view.findViewById(R.id.tv_songbook2_rank_list);
            top1 = (TextView) view.findViewById(R.id.tv_songbook2_rank_top1);
            top2 = (TextView) view.findViewById(R.id.tv_songbook2_rank_top2);
            top3 = (TextView) view.findViewById(R.id.tv_songbook2_rank_top3);
        }
    }





}
