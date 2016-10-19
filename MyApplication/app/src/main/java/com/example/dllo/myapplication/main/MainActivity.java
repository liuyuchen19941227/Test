package com.example.dllo.myapplication.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.adapter.FragmentAdapter;
import com.example.dllo.myapplication.base_class.BaseActivity;
import com.example.dllo.myapplication.base_class.MyApp;
import com.example.dllo.myapplication.detail.music_content.MusicActivity;
import com.example.dllo.myapplication.detail.musicplay.MusicBean;
import com.example.dllo.myapplication.detail.musicplay.MusicService;
import com.example.dllo.myapplication.detail.popup_window.MusicItemBean;
import com.example.dllo.myapplication.detail.popup_window.PopUpWindowRecyclerViewAdapter;
import com.example.dllo.myapplication.karaoke.KaraokeFragment;
import com.example.dllo.myapplication.live.LiveFragment;
import com.example.dllo.myapplication.my.MyFragment;
import com.example.dllo.myapplication.songbook.fragment.SongBookFragment;
import com.example.dllo.myapplication.songbook.tools.OnRecyclerViewItemListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;

public class MainActivity extends BaseActivity implements View.OnClickListener {


    private TabLayout tb;
    private ViewPager vp;
    private TextView tvSong;
    private TextView tvAuthor;
    private ImageView ivMusic;


    private ImageView ivPlay;
    private MusicService.MyBinder binder;

    // 歌曲的网址
    private String url;
    private String lrclink;
    private String song;
    private String author;
    private String img;
    private MyConnection connection;
    private ArrayList<String> musics;
    private int totalTime;
    private LinearLayout ll;
    private ArrayList<MusicItemBean> musicItemBeans;
    private MusicBean musicBean;
    private Intent intent;


    @Override
    protected int setLayout() {
        return R.layout.activity_main;// 找布局
    }

    @Override
    protected void initView() {


        tb = bindView(R.id.tb_main);
        vp = bindView(R.id.vp_main);


        // 底部整个控制栏的点击事件
        ll = bindView(R.id.ll_main);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("song", song);
                intent.putExtra("author", author);
                intent.putExtra("lrclink", lrclink);
                intent.putExtra("img", img);
                intent.putExtra("totalTime", totalTime);
                startActivity(intent);
            }
        });



        // 底部内容信息栏
        tvSong = bindView(R.id.tv_main_music);
        tvAuthor = bindView(R.id.tv_main_singer);
        ivMusic = bindView(R.id.iv_main_album);
        // 设置允许手动滚动
        tvSong.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvAuthor.setMovementMethod(ScrollingMovementMethod.getInstance());



        // 底部控制栏
        ivPlay = bindView(R.id.iv_main_play);
        ImageView ivNext = bindView(R.id.iv_main_next);
        ImageView ivList = bindView(R.id.iv_main_list);
        // 点击事件
        ivPlay.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivList.setOnClickListener(this);


        // 整个歌单的信息
        musics = new ArrayList<>();

        // 注册EventBus
        EventBus.getDefault().register(this);
        ShareSDK.initSDK(this,"sharesdk的appkey");


        // 在这开启服务!!!!!
        intent = new Intent(MyApp.getContext(), MusicService.class);
        startService(intent);
        connection = new MyConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);



//        Intent intent1 = getIntent();
//        if (intent1.getBooleanExtra("isPlay", true)) {
//            Log.d("MainActivity", "pause");
//            ivPlay.setImageResource(R.mipmap.pause);
//        } else {
//            Log.d("MainActivity", "play");
//            ivPlay.setImageResource(R.mipmap.play);
//        }





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }




    // EventBus接收数据,获取到传的值,添加了下面这句话,这个方法一定会执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendMsgEventBean sendMsgEvent) {

        // 歌曲的详细信息
        author = sendMsgEvent.getAuthor();
        song = sendMsgEvent.getSong();
        lrclink = sendMsgEvent.getLrclink();
        url = sendMsgEvent.getUrl();
        img = sendMsgEvent.getImg();
        musics = sendMsgEvent.getMusics();
        musicItemBeans = sendMsgEvent.getMusicItemBeanArrayList();
        musicBean = sendMsgEvent.getMusicBean();


        tvSong.setText(song);
        tvAuthor.setText(author);
        Picasso.with(this).load(img).into(ivMusic);

        ivPlay.setImageResource(R.mipmap.pause);

    }






    @Override
    protected void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyFragment());
        fragments.add(new SongBookFragment());
        fragments.add(new KaraokeFragment());
        fragments.add(new LiveFragment());

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        tb.setupWithViewPager(vp);
        tb.setSelectedTabIndicatorColor(Color.BLUE);
        tb.setTabTextColors(Color.GRAY, Color.WHITE);

        tb.getTabAt(0).setText("我的");
        tb.getTabAt(1).setText("乐库");
        tb.getTabAt(2).setText("动态");
        tb.getTabAt(3).setText("直播");

        vp.setCurrentItem(1);
        vp.setOffscreenPageLimit(2);



        // 数据持久化-接收
        SharedPreferences msgGetSp = getSharedPreferences("music", MODE_PRIVATE);
        song = msgGetSp.getString("song", "歌曲名");
        author = msgGetSp.getString("author", "作者名");
        img = msgGetSp.getString("img", "");
        url = msgGetSp.getString("songUrl", "");
        totalTime = msgGetSp.getInt("totalTime", 0);
        lrclink = msgGetSp.getString("lrcLink", "");
        Log.d("lrclink1", lrclink);


        tvSong.setText(song);
        tvAuthor.setText(author);
        if (!img.equals("")) {
            Picasso.with(this).load(img).into(ivMusic);
        }

        // 歌曲是要加载的,但是必须要点击播放才可以播放!


        // 他可能不是在这个位置的
        int playMode = msgGetSp.getInt("playMode", 0);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main_play:
//                startService(intent);
//                if (!url.equals("")) {
//                    binder.getMusicPlayer().playMusic(url);
//                    ivPlay.setImageResource(R.mipmap.pause);
//                    url = "";
//                } else
                if (binder.isPlay()) {
                    binder.pause();
                    ivPlay.setImageResource(R.mipmap.play);
                } else {
                    binder.play();
                    ivPlay.setImageResource(R.mipmap.pause);
                }
                break;
            case R.id.iv_main_next:
                binder.next();
                break;
            case R.id.iv_main_list:
                showPopUpWindow();
                break;
        }


    }


    // 显示MusicList - PopUpWindow()
    private void showPopUpWindow(){
        // 设置个透明度
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_window_my_songlist, null);
        PopupWindow ppWMusicList = new PopupWindow(contentView, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.rv_popup_window_mylist);

        ppWMusicList.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                // 要返回false
                return false;
            }
        });

        // 必须要设置,不然点击返回按钮和背景是不会退出popupWindow的
        ppWMusicList.setTouchable(true);
        ppWMusicList.setOutsideTouchable(true);
        ppWMusicList.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        // 接收一个传过来的解析好的数据,否则可能会前面的没数据,下拉再上拉刷新后才有
        PopUpWindowRecyclerViewAdapter adapter = new PopUpWindowRecyclerViewAdapter(this);
        adapter.setMusicItemBeen(musicItemBeans);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnRecyclerViewItemListener() {
            @Override
            public void onItemClick(View view, String id, int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                // 播放下一曲
                binder.getMusicPlayer().playMusic(musicItemBeans.get(position).getUrl());
                // 能播放,可是播放之后的刷新UI还没写

            }
        });



        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        // ppw设置显示位置(第二个参数:高度为距离底部的     )
        ppWMusicList.showAtLocation(vp, Gravity.BOTTOM, 0, 0);

        // 在某某下方显示
//        ppWMusicList.showAsDropDown(vp);

    }





    public class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (MusicService.MyBinder)iBinder;

            binder.sendBroadCast();

            if (binder.isPlay()) {
                Log.d("MyConnection", "pause?");
                binder.pause();
                ivPlay.setImageResource(R.mipmap.pause);
            } else {
                Log.d("MyConnection", "play?");
                binder.play();
                ivPlay.setImageResource(R.mipmap.play);
            }




        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }

    }




}
