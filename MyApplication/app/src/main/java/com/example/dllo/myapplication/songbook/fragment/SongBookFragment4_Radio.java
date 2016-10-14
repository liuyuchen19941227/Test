package com.example.dllo.myapplication.songbook.fragment;

import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.baseClass.BaseFragment;
import com.example.dllo.myapplication.baseClass.GsonRequest;
import com.example.dllo.myapplication.baseClass.MyApp;
import com.example.dllo.myapplication.baseClass.VolleySingleton;
import com.example.dllo.myapplication.songbook.adapter.SongBook4_VedioList_BaseAdapter;
import com.example.dllo.myapplication.songbook.bean.SongBook4_VedioBean;
import com.example.dllo.myapplication.songbook.tools.URLValues;

import java.util.ArrayList;

public class SongBookFragment4_Radio extends BaseFragment implements View.OnClickListener {

    private GridView gv;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main2_songbook3_musiclist;
    }

    @Override
    protected void initView() {

        gv = bindView(R.id.gv_main2_songbook3);

        TextView tvHottest = bindView(R.id.tv_songbook3_hotest);
        TextView tvNewest = bindView(R.id.tv_songbook3_newest);
        ImageView ivMore = bindView(R.id.iv_more);

        tvHottest.setOnClickListener(this);
        tvNewest.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        sendNewest();

    }


    private void sendNewest() {

        GsonRequest<SongBook4_VedioBean> gsonRequest = new GsonRequest<SongBook4_VedioBean>(
                URLValues.NEWESTVEDIO,
                SongBook4_VedioBean.class,
                new Response.Listener<SongBook4_VedioBean>() {

                    @Override
                    public void onResponse(SongBook4_VedioBean response) {

                        ArrayList<SongBook4_VedioBean> arrayList = new ArrayList<>();
                        arrayList.add(response);
                        SongBook4_VedioList_BaseAdapter adapter = new SongBook4_VedioList_BaseAdapter(MyApp.getContext());
                        adapter.setArrayList(arrayList);
                        gv.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SongBookFragment4_Radio", "error");
                    }
                }

        );

        VolleySingleton.getInstance().addRequest(gsonRequest);

    }



    private void sendHottest() {

        GsonRequest<SongBook4_VedioBean> gsonRequest = new GsonRequest<SongBook4_VedioBean>(
                URLValues.HOTTESTVEDIO,
                SongBook4_VedioBean.class,
                new Response.Listener<SongBook4_VedioBean>() {
                    @Override
                    public void onResponse(SongBook4_VedioBean response) {

                        ArrayList<SongBook4_VedioBean> arrayList = new ArrayList<>();
                        arrayList.add(response);
                        SongBook4_VedioList_BaseAdapter adapter = new SongBook4_VedioList_BaseAdapter(MyApp.getContext());
                        adapter.setArrayList(arrayList);
                        gv.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SongBookFragment4_Radio", "error");
                    }
                }
        );

        VolleySingleton.getInstance().addRequest(gsonRequest);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_songbook3_hotest:
                sendHottest();
                Log.d("SongBookFragment4_Radio", "hot");
                break;
            case R.id.tv_songbook3_newest:
                sendNewest();
                Log.d("SongBookFragment4_Radio", "new");
                break;

        }


    }
}
