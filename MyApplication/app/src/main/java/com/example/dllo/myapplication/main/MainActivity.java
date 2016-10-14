package com.example.dllo.myapplication.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.adapter.FragmentAdapter;
import com.example.dllo.myapplication.baseClass.BaseActivity;
import com.example.dllo.myapplication.baseClass.MyApp;
import com.example.dllo.myapplication.karaoke.KaraokeFragment;
import com.example.dllo.myapplication.live.LiveFragment;
import com.example.dllo.myapplication.my.MyFragment;
import com.example.dllo.myapplication.songbook.detail.music_content.MusicActivity;
import com.example.dllo.myapplication.songbook.detail.musicplay.MusicService;
import com.example.dllo.myapplication.songbook.fragment.SendMsgEventBean;
import com.example.dllo.myapplication.songbook.fragment.SongBookFragment;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
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

    @Override
    protected int setLayout() {
        return R.layout.activity_main;// 找布局
    }

    @Override
    protected void initView() {


        tb = bindView(R.id.tb_main);
        vp = bindView(R.id.vp_main);


        // 底部整个控制栏的点击事件
        LinearLayout ll = bindView(R.id.ll_main);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MusicActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("song", song);
                intent.putExtra("author", author);
                intent.putExtra("lrclink", lrclink);
                intent.putExtra("img", img);
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


        // 注册EventBus
        EventBus.getDefault().register(this);
        ShareSDK.initSDK(this,"sharesdk的appkey");




        // 在这开启服务
        Intent intent = new Intent(MyApp.getContext(), MusicService.class);
        startService(intent);
        connection = new MyConnection();
        bindService(intent, connection, BIND_AUTO_CREATE);


        // 推送
        // 拷贝到app2
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);

    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        Log.d("MainActivity", "1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        Log.d("MainActivity", "2");
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
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_main_play:
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
                Toast.makeText(this, "歌单列表", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    public class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("MyConnection", "运行了");
            binder = (MusicService.MyBinder)iBinder;
            if (binder.isPlay()) {
                binder.pause();
                ivPlay.setImageResource(R.mipmap.pause);
            } else {
                binder.play();
                ivPlay.setImageResource(R.mipmap.play);
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }




}
