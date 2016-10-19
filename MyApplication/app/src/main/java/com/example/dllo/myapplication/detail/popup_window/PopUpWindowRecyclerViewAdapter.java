package com.example.dllo.myapplication.detail.popup_window;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.songbook.tools.OnRecyclerViewItemListener;

import java.util.ArrayList;

public class PopUpWindowRecyclerViewAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private ArrayList<MusicItemBean> musicItemBeans;

    private OnRecyclerViewItemListener listener;


    public void setOnItemClickListener(OnRecyclerViewItemListener listener){
        this.listener = listener;
    }




    public PopUpWindowRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setMusicItemBeen(ArrayList<MusicItemBean> musicItemBeans) {
        this.musicItemBeans = musicItemBeans;
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popupwindow_my_songlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.song.setText(position +" - " + musicItemBeans.get(position).getSong());
        viewHolder.author.setText(musicItemBeans.get(position).getAuthor());

        // 相隔8个,重复出现一次....
        if (musicItemBeans.get(position).getIsPlay() == 1) {
            viewHolder.imgIsPlay.setImageResource(R.mipmap.ic_launcher);
        } else {
            // 设置一个默认为空的图片
            viewHolder.imgIsPlay.setImageResource(0);
        }



        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(viewHolder.rl, "",position);

            }
        });


    }



    @Override
    public int getItemCount() {
        return musicItemBeans == null ? 0 : musicItemBeans.size();
    }



    private class ViewHolder extends RecyclerView.ViewHolder{

        private TextView song;
        private TextView author;
        private ImageView imgIsPlay;
        private ImageView imgPlayMode;
        private RelativeLayout rl;



        public ViewHolder(View itemView) {
            super(itemView);
            song = (TextView) itemView.findViewById(R.id.tv_item_popupwindow_song);
            author = (TextView) itemView.findViewById(R.id.tv_item_popupwindow_author);
            imgIsPlay = (ImageView) itemView.findViewById(R.id.iv_popupwindow_isplay);
//            imgPlayMode = (ImageView) itemView.findViewById(R.id.iv_popupwindow_mylist_playmode);
            rl = (RelativeLayout) itemView.findViewById(R.id.rv_ppw_item);



        }
    }





}
