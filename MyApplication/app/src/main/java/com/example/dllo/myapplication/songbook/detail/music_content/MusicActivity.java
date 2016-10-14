package com.example.dllo.myapplication.songbook.detail.music_content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.adapter.FragmentAdapter;
import com.example.dllo.myapplication.baseClass.BaseActivity;
import com.example.dllo.myapplication.songbook.detail.musicplay.MusicService;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MusicActivity extends BaseActivity implements View.OnClickListener {


    private String url;
    private String result;
    private String link;
    private ViewPager vp;
    private MusicService.MyBinder binder;
    private ImageView iv_start;
    private MyConnection2 myConnection;
    private SeekBar seekBar;
    private TextView tv_music_total_time;
    private TextView tv_music_now;
    private Receiver receiver;
    private int[] values;
    private int duration;




    @Override
    protected int setLayout() {
        return R.layout.activity_music;
    }

    @Override
    protected void initView() {

        // 总时长初始化为0...
        duration = 0;

        // 服务
        // 绑定后要获取binder,并没有那么快,在下面MyConnection2方法里面执行操作
        Intent serviceIntent = new Intent(MusicActivity.this, MusicService.class);
        myConnection = new MyConnection2();
        bindService(serviceIntent, myConnection, BIND_AUTO_CREATE);


        // 广播
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("progress");
        registerReceiver(receiver, filter);


        // 接收传过来的数据
        Intent intent = getIntent();
        // 当前歌曲url
        String url = intent.getStringExtra("url");


        // 返回按钮
        ImageView iv_back = bindView(R.id.iv_music_back);
        iv_back.setOnClickListener(this);



        // 歌曲进度
        tv_music_now = bindView(R.id.tv_music_now);
        tv_music_total_time = bindView(R.id.tv_music_total_time);
        seekBar = bindView(R.id.music_seekBar);


        // 第一排按钮
        ImageView iv_like = bindView(R.id.iv_music_like);
        ImageView iv_download = bindView(R.id.iv_music_download);
        ImageView iv_share = bindView(R.id.iv_music_share);
        iv_share.setOnClickListener(this);
        ImageView iv_comment = bindView(R.id.iv_music_comment);
        ImageView iv_more = bindView(R.id.iv_music_more);


        // 第二排按钮
        ImageView iv_play_model = bindView(R.id.iv_music_play_model);
        ImageView iv_previous = bindView(R.id.iv_music_previous);
        iv_previous.setOnClickListener(this);
        iv_start = bindView(R.id.iv_music_play);
        iv_start.setOnClickListener(this);
        ImageView iv_next = bindView(R.id.iv_music_next);
        iv_next.setOnClickListener(this);
        ImageView iv_list = bindView(R.id.iv_music_list);


        vp = bindView(R.id.vp_music);

    }

    @Override
    protected void initData() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment_Music_Page1());
        fragments.add(new Fragment_Music_Page2());
        fragments.add(new Fragment_Music_Page3());

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        vp.setCurrentItem(1);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消绑定
        unbindService(myConnection);
        // 取消广播接收器的注册
        unregisterReceiver(receiver);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_music_back:
                this.finish();
                break;
            case R.id.iv_music_previous:
                binder.previous();
                iv_start.setImageResource(R.mipmap.pause);
                break;
            case R.id.iv_music_play:
                if (binder.isPlay()) {
                    binder.pause();
                    Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
                    iv_start.setImageResource(R.mipmap.play_black);
                } else {
                    binder.play();
                    Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show();
                    iv_start.setImageResource(R.mipmap.pause);
                }
                break;
            case R.id.iv_music_next:
                binder.next();
                iv_start.setImageResource(R.mipmap.pause);
                break;

            case R.id.iv_music_share:
                ShareSDK.initSDK(MusicActivity.this,"sharesdk的appkey");
                showShare();
                break;

        }
    }


    // 服务
    private class MyConnection2 implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (MusicService.MyBinder) iBinder;

            // 判断是否在播放状态
            if (binder.isPlay()) {
                Log.d("MusicActivity", "播放");
                iv_start.setImageResource(R.mipmap.pause);
            } else {
                Log.d("MusicActivity", "暂停");
                iv_start.setImageResource(R.mipmap.play_black);
            }

            // 总时长(毫秒为单位)
            duration = binder.getMusicPlayer().getTotalTime();
            tv_music_total_time.setText(changeTimeType(duration));




            // seekBar滑动监听
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    binder.getMusicPlayer().player.seekTo((int) (i / 100f * duration));
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }


    // 广播接收者
    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 当前时间
            int values = intent.getIntExtra("pro", 0);
            tv_music_now.setText(changeTimeType(values));
            int totalTime = intent.getIntExtra("totalTime", 0);
            // 取百分比
            int pro = values * 100 / totalTime;
            seekBar.setProgress(pro);
        }
    }



    // 时间类型转换为   分 : 秒
    private String changeTimeType(int duration) {
        int minute = duration / 1000 / 60;// 分钟数
        int second = duration / 1000 % 60;
        String m = String.valueOf(minute);
        String s = second >= 10 ? String.valueOf(second) : "0" + String.valueOf(second);
        return m + " : " + s;
    }



    // 分享
    public void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }



}
