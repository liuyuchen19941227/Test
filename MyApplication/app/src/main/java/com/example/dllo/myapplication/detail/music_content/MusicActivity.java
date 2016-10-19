package com.example.dllo.myapplication.detail.music_content;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.adapter.FragmentAdapter;
import com.example.dllo.myapplication.base_class.BaseActivity;
import com.example.dllo.myapplication.base_class.VolleySingleton;
import com.example.dllo.myapplication.database.DBTools;
import com.example.dllo.myapplication.detail.musicplay.MusicBean;
import com.example.dllo.myapplication.detail.musicplay.MusicService;
import com.example.dllo.myapplication.main.MainActivity;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MusicActivity extends BaseActivity implements View.OnClickListener {


    private ViewPager vp;
    private MusicService.MyBinder binder;
    private ImageView iv_start;
    private MyConnection2 myConnection;
    private SeekBar seekBar;
    private TextView tv_music_total_time;
    private TextView tv_music_now;
    private Receiver receiver;
    private ImageView iv_play_model;
    private int model;
    private String urlSharePreference;
    private SharedPreferences.Editor msgSpEditor;
    private ImageView iv_like;
    private Boolean isLike;

    private MusicBean musicBean;
    private DBTools dbTools;


    @Override
    protected int setLayout() {
        return R.layout.activity_music;
    }

    @Override
    protected void initView() {

        // 总时长初始化为0...
        //        duration = 0;

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
        urlSharePreference = intent.getStringExtra("url");
        int totalTime = intent.getIntExtra("totalTime", 0);


        // 返回按钮
        ImageView iv_back = bindView(R.id.iv_music_back);
        iv_back.setOnClickListener(this);


        // 歌曲进度, 进度条
        tv_music_now = bindView(R.id.tv_music_now);
        //        tv_music_now.setText("00 : 00");
        tv_music_total_time = bindView(R.id.tv_music_total_time);
        tv_music_total_time.setText(changeTimeType(totalTime));
        seekBar = bindView(R.id.music_seekBar);


        // 第一排按钮
        isLike = false;
        iv_like = bindView(R.id.iv_music_like);
        iv_like.setOnClickListener(this);
        ImageView iv_download = bindView(R.id.iv_music_download);
        iv_download.setOnClickListener(this);
        ImageView iv_share = bindView(R.id.iv_music_share);
        iv_share.setOnClickListener(this);
        ImageView iv_comment = bindView(R.id.iv_music_comment);
        ImageView iv_more = bindView(R.id.iv_music_more);


        // 第二排按钮
        iv_play_model = bindView(R.id.iv_music_play_model);
        iv_play_model.setOnClickListener(this);
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
        fragments.add(new Fragment_Music_Page1_Recommend());
        fragments.add(new Fragment_Music_Page2_Detail());
        fragments.add(new Fragment_Music_Page3_Lyric());

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        vp.setCurrentItem(1);
        // 一次可以加载两页的方法
        vp.setOffscreenPageLimit(2);


        // 利用持久化技术取出播放模式数据
        SharedPreferences getMsgSP = getSharedPreferences("music", MODE_PRIVATE);
        msgSpEditor = getMsgSP.edit();
        model = getMsgSP.getInt("playMode", 0);
        Log.d("model1", "model:" + model);

        dbTools = new DBTools(this);

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
                finish();
                Intent intent1 = new Intent(MusicActivity.this, MainActivity.class);
                intent1.putExtra("isPlay", binder.getMusicPlayer().isPlay());
                //                startActivity(intent1);
                overridePendingTransition(R.anim.dialog_enter, R.anim.dialog_exit);

                break;
            case R.id.iv_music_previous:
                binder.previous();
                iv_start.setImageResource(R.mipmap.pause);
                break;
            case R.id.iv_music_play:
                // 持久化技术的播放问题

                //                if (!urlSharePreference.equals("")) {
                //                    iv_start.setImageResource(R.mipmap.pause);
                //                    binder.getMusicPlayer().playMusic(urlSharePreference);
                //                    urlSharePreference = "";
                //                } else

                if (binder.isPlay()) {
                    binder.pause();
                    Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
                    iv_start.setImageResource(R.mipmap.play_black);

                    View view1 = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
                    ImageView iv = (ImageView) view1.findViewById(R.id.iv_main_play);
                    iv.setImageResource(R.mipmap.play);


                } else {
                    binder.play();
                    Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show();
                    iv_start.setImageResource(R.mipmap.pause);


                    View view1 = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
                    ImageView iv = (ImageView) view1.findViewById(R.id.iv_main_play);
                    iv.setImageResource(R.mipmap.pause);

                }
                break;
            case R.id.iv_music_next:
                binder.next();
                iv_start.setImageResource(R.mipmap.pause);
                break;

            case R.id.iv_music_share:
                ShareSDK.initSDK(MusicActivity.this, "sharesdk的appkey");
                showShare();
                break;
            // 播放模式
            case R.id.iv_music_play_model:
                // 应该在这把参数传到服务里面
                model++;
                // 持久化技术,存储播放模式
                msgSpEditor.putInt("playMode", model);
                // 每次修改数据之后必须要加上这句话
                msgSpEditor.commit();
                judgePlayModel();
                break;
            case R.id.iv_music_like:
                isLike = !isLike;
                if (isLike) {
                    // 如果点击后变成喜欢
                    iv_like.setImageResource(R.mipmap.like_red);

                } else {
                    // 如果标称不喜欢
                    iv_like.setImageResource(R.mipmap.like);
                }
                break;
            case R.id.iv_music_download:
                // 取出当前播放歌曲MusicBean的全部信息
                downLoadMusic(binder.getMusicBeanAll());
                break;
        }
    }


    // 主线程不能进行解析的耗时操作
    // 单曲解析
    private void sendGet(String url) {
        Log.d("MusicActivity22222", url);
        // 换成StringRequest 获取那些数据,然后减掉
        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MusicActivity", response);// null
                        response = response.substring(1, response.length() - 2);
                        Log.d("MusicActivity", response);
                        Gson gson = new Gson();
                        musicBean = new MusicBean();
                        musicBean = gson.fromJson(response, MusicBean.class);
                        Log.d("MusicActivity", musicBean.getSonginfo().getTitle());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        VolleySingleton.getInstance().addRequest(request);
        downLoadMusic(musicBean);
    }


    private void downLoadMusic(MusicBean musicBean) {
        if (musicBean != null && musicBean.getBitrate() != null && musicBean.getBitrate().getFile_link() != null) {
            Log.d("DBTools1", musicBean.getSonginfo().getSong_id());

            if (dbTools.queryDB(musicBean.getSonginfo().getSong_id())) {
                Toast.makeText(this, "该歌曲已被下载", Toast.LENGTH_SHORT).show();
                return;
            }

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(musicBean.getBitrate().getFile_link());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            File folder = new File("/BaiduMusic/music");
            if (!(folder.exists() && folder.isDirectory())) {
                folder.mkdirs();// 递归式创建路径
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, musicBean.getSonginfo().getTitle() + "-" + musicBean.getSonginfo().getAuthor() + ".mp3");
            // 提示正在下载
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);


            DownloadMusicBean downloadMusicBean = new DownloadMusicBean();
            // 队列编号
            long downloadId = downloadManager.enqueue(request);
//            downloadMusicBean.setId(downloadId);
            downloadMusicBean.setSong(musicBean.getSonginfo().getTitle());
            downloadMusicBean.setAuthor(musicBean.getSonginfo().getAuthor());
            downloadMusicBean.setSongId(musicBean.getSonginfo().getSong_id());// 直接把song_id存进去
            downloadMusicBean.setMusicUrl(musicBean.getBitrate().getFile_link());
            downloadMusicBean.setImgUrl(musicBean.getSonginfo().getPic_radio());
            downloadMusicBean.setLrcLink(musicBean.getSonginfo().getLrclink());
            // 插入数据库
            dbTools.insertDB(musicBean);

        }
    }





    public void judgePlayModel() {
        Log.d("model2", "model:" + model);
        int result = model % 4;
        if (result == MusicValues.PLAYMODE_LOOPPLAY) {
            // 循环播放
            iv_play_model.setImageResource(R.mipmap.play_model_loop_normal);
            binder.setPlayModel(model);
        } else if(result == MusicValues.PLAYMODE_ORDERPLAY) {
            // 顺序播放
            iv_play_model.setImageResource(R.mipmap.play_model_order_normal);
            binder.setPlayModel(model);
        } else if (result == MusicValues.PLAYMODE_RANDOMPLAY){
            // 随机播放
            iv_play_model.setImageResource(R.mipmap.play_model_random);
            binder.setPlayModel(model);
        } else if (result == MusicValues.PLAYMODE_SINGLEPLAY){
            // 单曲循环
            iv_play_model.setImageResource(R.mipmap.play_model_sigle_circle);
            binder.setPlayModel(model);
            Log.d("MusicActivity", "单曲循环");
        }
    }



    // 服务
    private class MyConnection2 implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (MusicService.MyBinder) iBinder;

            // 设置最大播放长度
            // seekBar.setMax(binder.getMusicTotalDuration());

            judgePlayModel();
            Log.d("model3", "model:" + model);
            //            binder.setPlayModel(model);

            // seekBar的播放进度
            // 不断地去请求数据,而不是让服务去发送数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        // 需要判断一下
                        if (binder.isPlay()) {
                            seekBar.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 以百分比的形式显示
                                    seekBar.setProgress(binder.getMusicCurrentP() * 100 / binder.getMusicTotalDuration());
                                    tv_music_now.setText(changeTimeType(binder.getMusicCurrentP()));
                                    tv_music_total_time.setText(changeTimeType(binder.getMusicTotalDuration()));

                                }
                            });
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();


            // 判断是否在播放状态
            if (binder.isPlay()) {
                Log.d("MusicActivity", "播放");
                iv_start.setImageResource(R.mipmap.pause);
            } else {
                Log.d("MusicActivity", "暂停");
                iv_start.setImageResource(R.mipmap.play_black);
            }

            // 总时长(毫秒为单位)
            //            duration = binder.getMusicPlayer().getTotalTime();
            // tv_music_total_time.setText(changeTimeType(duration));
            // tv_music_total_time.setText(binder.getMusicTotalDuration());


            // seekBar滑动监听
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // 判断是否是用户滑动的...
                    if (fromUser) {
                        binder.getMusicPlayer().player.seekTo((int) (progress / 100f * binder.getMusicTotalDuration()));
                    }
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
            //            int values = intent.getIntExtra("pro", 0);
            //            tv_music_now.setText(changeTimeType(values));

            //            int totalTime = intent.getIntExtra("totalTime", 0);
            //            Log.d("Receiver", "totalTime:" + totalTime);
            //            tv_music_total_time.setText(changeTimeType(totalTime));


            //            // 取百分比
            //            int pro = values * 100 / totalTime;
            //            seekBar.setProgress(pro);


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
