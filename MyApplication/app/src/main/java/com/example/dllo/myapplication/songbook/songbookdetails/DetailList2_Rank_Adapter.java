package com.example.dllo.myapplication.songbook.songbookdetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.songbook.tools.OnRecyclerViewItemListener;
import com.example.dllo.myapplication.songbook.tools.URLValues;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailList2_Rank_Adapter extends RecyclerView.Adapter{


    private Context mContext;
    private ArrayList<DetailList2_Rank_Bean> arrayList;

    private OnRecyclerViewItemListener listener;

    public void onItemClickListener(OnRecyclerViewItemListener listener) {
        this.listener = listener;
    }



    public DetailList2_Rank_Adapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(ArrayList<DetailList2_Rank_Bean> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list2_detail_rank, parent, false);
        ViewHolder viewHodler = new ViewHolder(view);
        return viewHodler;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        Log.d("DetailList2_Rank", arrayList.get(0).getSong_list().get(position).getTitle());

        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvSong.setText(arrayList.get(0).getSong_list().get(position).getTitle());
        viewHolder.tvAuthor.setText(arrayList.get(0).getSong_list().get(position).getAuthor());
        Picasso.with(mContext).load(arrayList.get(0).getSong_list().get(position).getPic_big()).into(viewHolder.ivSongImg);



        if (arrayList.get(0).getSong_list().get(position).getHas_mv() == 1) {
            viewHolder.isHasMV.setImageResource(R.mipmap.mv);
            LinearLayout.LayoutParams ps = (LinearLayout.LayoutParams) viewHolder.isHasMV.getLayoutParams();
            ps.height = 20;
            ps.width = 45;
            viewHolder.isHasMV.setLayoutParams(ps);

        }







        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url = URLValues.PLAY_FRONT + arrayList.get(0).getSong_list().get(position).getSong_id() + URLValues.PLAY_BEHIND;


                Log.d("DetailList2_Rank_Adapte", url);

                listener.onItemClick(viewHolder.rl, url, position);



            }
        });


    }



    @Override
    public int getItemCount() {
        // 一共是50条数据,添加下拉刷新
        return arrayList.get(0).getSong_list().size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivSongImg;
        private TextView tvSong;
        private TextView tvAuthor;
        private ImageView isHasMV;
        private ImageView isHasSQ;
        private ImageView isHasKaraoke;

        private RelativeLayout rl;



        public ViewHolder(View itemView) {
            super(itemView);

            ivSongImg = (ImageView) itemView.findViewById(R.id.iv_list2_detail_rank_song_img);
            tvSong = (TextView) itemView.findViewById(R.id.tv_list2_detail_song1);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_list2_detail_author111);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl_list2_detail_rank);

            isHasMV = (ImageView) itemView.findViewById(R.id.iv_ishas2_mv);
            isHasSQ = (ImageView) itemView.findViewById(R.id.iv_ishas2_sq);
            isHasKaraoke = (ImageView) itemView.findViewById(R.id.iv_ishas2_karaoke);


        }
    }


}
