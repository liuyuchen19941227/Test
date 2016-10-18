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

import java.util.ArrayList;

public class DetailList1_Recommend1_Adapter extends RecyclerView.Adapter{

    private Context mContext;
    private ArrayList<DetailList1_Recommend1_Bean> arrayList;

    private OnRecyclerViewItemListener listener;

    // 公有方法
    public void setOnItemListener(OnRecyclerViewItemListener listener){
        this.listener = listener;
    }


    public DetailList1_Recommend1_Adapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setArrayList(ArrayList<DetailList1_Recommend1_Bean> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list1_detail1_recommendlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Log.d("DetailList1_Adapter", arrayList.get(0).getContent().get(position).getTitle());



        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvSong.setText(arrayList.get(0).getContent().get(position).getTitle());
        viewHolder.tvAuthor.setText(arrayList.get(0).getContent().get(position).getAuthor());

        // 1有  0 没有
        if (arrayList.get(0).getContent().get(position).getIs_ksong().equals("1")) {
            // 显示
            viewHolder.ivIsHasKaraoke.setImageResource(R.mipmap.ksong);
        }

        if (arrayList.get(0).getContent().get(position).getHas_mv() == 1){
            viewHolder.ivIsHasMv.setImageResource(R.mipmap.mv);

            LinearLayout.LayoutParams ps = (LinearLayout.LayoutParams) viewHolder.ivIsHasMv.getLayoutParams();
            ps.height = 20;
            ps.width = 45;
            viewHolder.ivIsHasMv.setLayoutParams(ps);





        }


        // Intent传一个对象过去
        // (Java的) 类 implements Serilizable  接口就可以用intent传   getSerializableExtra("key值");
        // (安卓的) 类 implements Parceable(包裹化)
        // getIntent().getParcelableExtra("key值") 有内部类的话,先把内部类实现这个方法,再去实现外层的(writeToParse)




        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = URLValues.PLAY_FRONT + arrayList.get(0).getContent().get(position).getSong_id() + URLValues.PLAY_BEHIND;

                Log.d("DetailList1_Recommend1_", url);

                listener.onItemClick(viewHolder.rl, url, position);

            }
        });




    }

    @Override
    public int getItemCount() {
        return arrayList.get(0).getContent() == null ? 0 : arrayList.get(0).getContent().size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvSong;
        private TextView tvAuthor;
        private ImageView ivIsHasMv;
        private ImageView ivIsHasSQ;
        private ImageView ivIsHasKaraoke;
        private RelativeLayout rl;



        public ViewHolder(View itemView) {
            super(itemView);

            tvSong = (TextView) itemView.findViewById(R.id.tv_list_detail_song1);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_list_detail_author111);
            ivIsHasMv = (ImageView) itemView.findViewById(R.id.iv_ishas_mv);
            ivIsHasSQ = (ImageView) itemView.findViewById(R.id.iv_ishas_sq);
            ivIsHasKaraoke = (ImageView) itemView.findViewById(R.id.iv_ishas_karaoke);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl_list_detail_recommendlist);

        }


    }




}
