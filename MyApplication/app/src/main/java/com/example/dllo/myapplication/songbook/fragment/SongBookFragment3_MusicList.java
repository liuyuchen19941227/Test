package com.example.dllo.myapplication.songbook.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseFragment;
import com.example.dllo.myapplication.base_class.GsonRequest;
import com.example.dllo.myapplication.base_class.MyApp;
import com.example.dllo.myapplication.base_class.VolleySingleton;
import com.example.dllo.myapplication.songbook.adapter.SongBook3_SongList_BaseAdapter;
import com.example.dllo.myapplication.songbook.bean.SongBook3_ListBean;
import com.example.dllo.myapplication.songbook.songbookdetails.DetailList1_Recommend1_MusicListRecommend;
import com.example.dllo.myapplication.songbook.tools.URLValues;

import java.util.ArrayList;


public class SongBookFragment3_MusicList extends BaseFragment implements View.OnClickListener {

    private GridView gv;
    private TextView ivHottest;
    private TextView ivNewest;
    private ImageView ivMore;

    private ArrayList<SongBook3_ListBean> arrayList;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main2_songbook3_musiclist;
    }

    @Override
    protected void initView() {

        gv = bindView(R.id.gv_main2_songbook3);
        ivHottest = bindView(R.id.tv_songbook3_hotest);
        ivNewest = bindView(R.id.tv_songbook3_newest);
        ivMore = bindView(R.id.iv_more);


        ivNewest.setOnClickListener(this);
        ivHottest.setOnClickListener(this);
        ivMore.setOnClickListener(this);

    }

    @Override
    protected void initData() {


//        sendGetNewest();
        sendGetHotest();// 默认显示最热

        // 放在这个位置不太好...
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String id = arrayList.get(0).getDiyInfo().get(i).getList_id();
                Log.d("SongBookFragment3_Music", "url:" + id);


                String url = URLValues.SONGLIST_DETAIL_Front + id + URLValues.SONGLIST_DETAIL_BEHIND;
                Log.d("SongBookFragment3_Music", url);


                DetailList1_Recommend1_MusicListRecommend fragment = new DetailList1_Recommend1_MusicListRecommend();
                // Bundle发送数据
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                fragment.setArguments(bundle);
                // Fragment切换并添加动画效果
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.list_detail_enter, R.anim.list_detail_exit).addToBackStack(null).replace(R.id.fl_main, fragment).commit();



            }
        });



    }


    private void sendGetNewest(){
        GsonRequest<SongBook3_ListBean> gsonRequest = new GsonRequest<SongBook3_ListBean>(
                URLValues.NEWESTMUSICLIST,
                SongBook3_ListBean.class,
                new Response.Listener<SongBook3_ListBean>() {
                    @Override
                    public void onResponse(SongBook3_ListBean response) {
                        arrayList = new ArrayList<>();
                        arrayList.add(response);
                        SongBook3_SongList_BaseAdapter adapter = new SongBook3_SongList_BaseAdapter(MyApp.getContext());
                        adapter.setBeans(arrayList);
                        gv.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SongBookFragment3_Music", "error");
                    }
                }
        );
        VolleySingleton.getInstance().addRequest(gsonRequest);
    }


    private void sendGetHotest(){
        GsonRequest<SongBook3_ListBean> gsonRequest = new GsonRequest<SongBook3_ListBean>(
                URLValues.HOTESTMUSICLIST,
                SongBook3_ListBean.class,
                new Response.Listener<SongBook3_ListBean>() {

                    @Override
                    public void onResponse(SongBook3_ListBean response) {
                        arrayList = new ArrayList<>();
                        arrayList.add(response);
                        SongBook3_SongList_BaseAdapter adapter = new SongBook3_SongList_BaseAdapter(MyApp.getContext());
                        adapter.setBeans(arrayList);
                        gv.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SongBookFragment3_Music", "error");
                    }
                }
        );
        VolleySingleton.getInstance().addRequest(gsonRequest);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_songbook3_hotest:
                Toast.makeText(mContext, "hot", Toast.LENGTH_SHORT).show();
                sendGetHotest();
                break;
            case R.id.tv_songbook3_newest:
                Toast.makeText(mContext, "new", Toast.LENGTH_SHORT).show();
                sendGetNewest();
                break;
            case R.id.iv_more:

                // 绑定布局
                View view1 = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.dialog_bottom, null);

//                AlertDialog dialog = new AlertDialog.Builder(getContext())
//                        .setTitle("title").setMessage("message").create();

                // 底部的dialog
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setView(view1);

                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
                window.setWindowAnimations(R.style.myStyle);  //添加动画
                dialog.show();

        }
    }








}
