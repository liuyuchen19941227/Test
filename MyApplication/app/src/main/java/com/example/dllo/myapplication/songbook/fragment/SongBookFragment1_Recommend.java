package com.example.dllo.myapplication.songbook.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseFragment;
import com.example.dllo.myapplication.base_class.GsonRequest;
import com.example.dllo.myapplication.base_class.MyApp;
import com.example.dllo.myapplication.base_class.VolleySingleton;
import com.example.dllo.myapplication.detail.musicplay.MusicService;
import com.example.dllo.myapplication.songbook.adapter.SongBook1_Recommend_RecyclerAdapter;
import com.example.dllo.myapplication.songbook.bean.CarouselFigureBean;
import com.example.dllo.myapplication.songbook.songbookdetails.DetailList1_Recommend1_MusicListRecommend;
import com.example.dllo.myapplication.songbook.tools.OnRecyclerViewItemListener;
import com.example.dllo.myapplication.songbook.tools.URLValues;
import com.youth.banner.Banner;

import java.util.ArrayList;

public class SongBookFragment1_Recommend extends BaseFragment {


    // 轮播图
    private int currentItem;
    private ArrayList<ImageView> imageViews;

    private Context mContext;
    private ViewPager vp;
    private Banner banner;
    private ArrayList<CarouselFigureBean> arrayList;
    private RecyclerView rv;

    private SwipeRefreshLayout sr;
    private ImageLoader imageLoader;
    private ImageView img1_1;
    private ImageView img1_2;
    private SongBook1_Recommend_RecyclerAdapter adapter;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main2_songbook1_recommend;
    }

    @Override
    protected void initView() {


        // 本布局中找rv
        rv = bindView(R.id.rv_songbook1_recommend);
        banner = bindView(R.id.banner_songbook1_recommend);
        sr = bindView(R.id.sr_songbook1);


        // 无法添加点击事件
        //        View v1 = LayoutInflater.from(mContext).inflate(R.layout.item_songbook1_recommend1_threeimg, null);
        //        img1_1 = bindView(R.id.iv_songbook1_recommend_allsinger, v1);
        //        img1_2 = bindView(R.id.iv_songbook1_recommend_allkinds, v1);


        // 下拉刷新
        sr.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_blue_light, android.R.color.holo_purple);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新时的刷新数据
                sendGet();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApp.getContext(), "刷新完成", Toast.LENGTH_SHORT).show();
                        sr.setRefreshing(false);
                    }
                }, 1000);
            }
        });


        // 去找布局和内部的vp
        //        View cfView = LayoutInflater.from(mContext).inflate(R.layout.carousel_figure, null);
        //        vp = bindView(R.id.vp_cf, cfView);

        // recyclerView不能加HeadView
        //        rv.addHeaderView(cfView);
        //        // 必须要绑定adapter
        //        SongBook2_Rank_BaseAdapter adapter = new SongBook2_Rank_BaseAdapter(mContext);
        //        rv.setAdapter(adapter);

    }


    @Override
    protected void initData() {

        // 给vp赋值,轮播图
        //        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        //        Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //        Bitmap img2 = BitmapFactory.decodeResource(getResources(), R.mipmap.dog);
        //        bitmaps.add(img);
        //        bitmaps.add(img2);
        //        bitmaps.add(img);
        //        imageViews = new ArrayList<>();// ArrayList<ImageView>
        //        for (int i = 0; i < bitmaps.size(); i++) {
        //            ImageView iv = new ImageView(getContext());
        //            iv.setImageBitmap(bitmaps.get(i));
        //            Log.d("SongBookFragment1_Recom", "iv:" + iv);
        //            imageViews.add(iv);
        //            Log.d("SongBookFragment1_Recom", "imageViews:" + imageViews);
        //        }
        //        vp.setAdapter(new CFPageAdapter(getContext(), imageViews));
        //        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        //            @Override
        //            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //            }
        //            @Override
        //            public void onPageSelected(int position) {
        //                currentItem = position;
        //            }
        //            @Override
        //            public void onPageScrollStateChanged(int state) {
        //            }
        //        });


        sendGet();


    }


    private void sendGet() {

        GsonRequest<CarouselFigureBean> gsonRequest = new GsonRequest<CarouselFigureBean>(
                URLValues.RECOMMAND_MAIN,
                CarouselFigureBean.class,
                new Response.Listener<CarouselFigureBean>() {
                    @Override
                    public void onResponse(final CarouselFigureBean response) {


                        arrayList = new ArrayList<>();
                        // 添加数据
                        arrayList.add(response);


                        adapter = new SongBook1_Recommend_RecyclerAdapter(MyApp.getContext());
                        adapter.setArrayList(arrayList);
                        adapter.setBean(response);

                        LinearLayoutManager manager = new LinearLayoutManager(MyApp.getContext());
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        rv.setLayoutManager(manager);

                        rv.setAdapter(adapter);


                        // 点击的回调方法 为什么不会报空指针异常
                        adapter.setOnItemClickListener(new OnRecyclerViewItemListener() {
                            @Override
                            public void onItemClick(View view, String url, int position) {
                                // 添加跳转事件
                                Log.d("SongBookFragment1_Recom", url);

                                int id = 0;

                                if (position == 10) {
                                    ArrayList<String> arrayList1 = new ArrayList<String>();
                                    for (int i = 0; i < response.getResult().getRecsong().getResult().size(); i++) {
                                        arrayList1.add(URLValues.PLAY_FRONT + response.getResult().getRecsong().getResult().get(i).getSong_id() + URLValues.PLAY_BEHIND);
                                        // 用逻辑的办法找到点击歌曲的位置......
                                        if (url.equals(URLValues.PLAY_FRONT + response.getResult().getRecsong().getResult().get(i).getSong_id() + URLValues.PLAY_BEHIND)) {
                                            id = i;
                                        }
                                        Log.d("SongBookFragment1_Recom", arrayList1.get(i));
                                    }

                                    // 交给服务去做
                                    Intent intent = new Intent(MyApp.getContext(), MusicService.class);
                                    intent.putExtra("url", url);
                                    intent.putExtra("position", id);
                                    intent.putExtra("urlMore", arrayList1);
                                    getActivity().startService(intent);


                                    // EventBus传到MainActivity,在那里面更新服务


                                } else if (position == 4){
                                    // 只需要position这个参数和当前url就够了,不需要位置和总共的List

                                    DetailList1_Recommend1_MusicListRecommend fragment = new DetailList1_Recommend1_MusicListRecommend();
                                    // Bundle发送数据
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", url);
//                                    bundle.putInt("position", id);//

                                    fragment.setArguments(bundle);
                                    // Fragment切换并添加动画效果
                                    // getFragmentManager().beginTransaction().setCustomAnimations(R.anim.list_detail_enter, R.anim.list_detail_exit).replace(R.id.fl_main, fragment).commit();
                                    // 等同于上面的方法
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    // 动画效果添加到哪里来着
                                    transaction.setCustomAnimations(R.anim.list_detail_enter, R.anim.list_detail_exit);
                                    transaction.replace(R.id.fl_main, fragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();




                                }

                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SongBookFragment1_Recom", "error");
                    }
                }


        );

        // 添加请求队列
        VolleySingleton.getInstance().addRequest(gsonRequest);


        //        // 设置缓存
        //        RequestQueue queue = Volley.newRequestQueue(mContext);
        //        imageLoader = new ImageLoader(queue, new DoubleCache(mContext));
        //        getImage();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }








    /*

    private ScheduledExecutorService service;
    @Override
    public void onStart() {
        super.onStart();
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new ViewPagerTask(), 2, 2, TimeUnit.SECONDS);
    }


    private class ViewPagerTask implements Runnable{
        @Override
        public void run() {
            // imageViews?
            currentItem = (currentItem + 1) % imageViews.size();
            Log.d("ViewPagerTask", "currentItem:" + currentItem);
            mHandler.sendEmptyMessage(0);
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            vp.setCurrentItem(currentItem);
        }
    };


    @Override
    public void onStop() {
        super.onStop();
        if (service != null) {
            service.shutdown();
            service = null;
        }
    }

    */


}
