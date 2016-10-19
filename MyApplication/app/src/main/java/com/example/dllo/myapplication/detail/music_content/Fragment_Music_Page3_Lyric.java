package com.example.dllo.myapplication.detail.music_content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseFragment;
import com.example.dllo.myapplication.detail.music_content.lyric.ILrcBuilder;
import com.example.dllo.myapplication.detail.music_content.lyric.DefaultLrcBuilder;
import com.example.dllo.myapplication.detail.music_content.lyric.ILrcView;
import com.example.dllo.myapplication.detail.music_content.lyric.ILrcViewListener;
import com.example.dllo.myapplication.detail.music_content.lyric.LrcRow;
import com.example.dllo.myapplication.detail.musicplay.MusicService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_Music_Page3_Lyric extends BaseFragment {

    private Receiver receiver;


//    public final static String TAG = "Page3";

    //自定义LrcView，用来展示歌词
    ILrcView mLrcView;
    //更新歌词的频率，每秒更新一次
    private int mPalyTimerDuration = 1000;
    //更新歌词的定时器
    private Timer mTimer;
    //更新歌词的定时任务
    private TimerTask mTask;


    private MusicService.MyBinder myBinder;
    private MyConnection2 myConnection;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private String lrc;


    @Override
    protected int setLayout() {
        return R.layout.fragment_music_page3_lyric;
    }

    @Override
    protected void initView() {
        mLrcView = bindView(R.id.lrcView);
    }


    @Override
    protected void initData() {

        // 广播
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("progress");
        getActivity().registerReceiver(receiver, filter);


        // 服务
        Intent serviceIntent = new Intent(getActivity(), MusicService.class);
        myConnection = new MyConnection2();
        getActivity().bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE);


        SharedPreferences sp = getActivity().getSharedPreferences("music", Context.MODE_PRIVATE);
        String lrc =  sp.getString("lrcLink", "");



        if (!lrc.equals("")){
            getFromAssets(lrc);
            // set歌词
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if (message.what == 1) {
                        String str = (String) message.obj;
                        //解析歌词构造器
                        ILrcBuilder builder = new DefaultLrcBuilder();
                        //解析歌词返回LrcRow集合
                        List<LrcRow> rows = builder.getLrcRows(str);
                        //将得到的歌词集合传给mLrcView用来展示1
                        mLrcView.setLrc(rows);
                    }
                    return false;
                }
            });
            // 设置歌词当前位置
            mLrcView.seekLrcToTime(0);
        }




    }


    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String lrcLink = intent.getStringExtra("lrclink");
            int currentTime = intent.getIntExtra("pro", 0);
            int totalTime = intent.getIntExtra("totalTime", 0);
            Log.d("Page3", lrcLink + " " + currentTime + " " + totalTime);


            // 去获取歌词
            getFromAssets(lrcLink);
            // set歌词
            handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if (message.what == 1) {
                        String str = (String) message.obj;
                        //解析歌词构造器
                        ILrcBuilder builder = new DefaultLrcBuilder();
                        //解析歌词返回LrcRow集合
                        List<LrcRow> rows = builder.getLrcRows(str);
                        //将得到的歌词集合传给mLrcView用来展示1
                        mLrcView.setLrc(rows);
                    }
                    return false;
                }
            });
            // 设置歌词当前位置
            mLrcView.seekLrcToTime(currentTime);



        }
    }


    private class MyConnection2 implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = (MusicService.MyBinder) iBinder;
            mediaPlayer = myBinder.getMusicPlayer().player;

            //设置自定义的LrcView上下拖动歌词时监听1
            mLrcView.setListener(new ILrcViewListener() {
                //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
                public void onLrcSeeked(int newPosition, LrcRow row) {
                    if (mediaPlayer != null) {
                        Log.d("Page3", "onLrcSeeked:" + row.time);
                        mediaPlayer.seekTo((int) row.time);
                    }
                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消注册
        getActivity().unregisterReceiver(receiver);
        getActivity().unbindService(myConnection);

        // 问题出在这!销毁页面,服务的对象不能销毁啊!!!!!!
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//        }
    }


    /**
     * 从assets目录下读取歌词文件内容
     *
     * @param
     * @return
     */
    public void getFromAssets(final String str) {
        // 不必用json去解析,只要一行一行去读流就可以了
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(str);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStream stream = connection.getInputStream();
                        InputStreamReader inputReader = new InputStreamReader(stream);
                        BufferedReader bufReader = new BufferedReader(inputReader);
                        String line = "";
                        String result = "";
                        while ((line = bufReader.readLine()) != null) {
                            // 取出字符左右两边的空格
                            if (line.trim().equals(""))
                                continue;
                            result += line + "\r\n";
                        }
                        bufReader.close();
                        inputReader.close();
                        stream.close();


                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        handler.sendMessage(msg);


                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


//    MediaPlayer mPlayer;

    /**
     * 开始播放歌曲并同步展示歌词
     */

    /*
    public void beginLrcPlay() {

        try {

            //准备播放歌曲监听 0
            // 读seekBar的百分比

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                //准备完毕
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    if (mTimer == null) {
                        mTimer = new Timer();
                        mTask = new LrcTask();
                        mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
                    }
                }
            });

            //歌曲播放完毕监听
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    stopLrcPlay();
                }
            });






//            //准备播放歌曲
//            mediaPlayer.prepare();
//            //开始播放歌曲
//            mediaPlayer.start();





        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    */



    /**
     * 停止展示歌曲
     */
    public void stopLrcPlay() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 展示歌曲的定时任务
     */
    class LrcTask extends TimerTask {
        @Override
        public void run() {
            //获取歌曲播放的位置
            final long timePassed = mediaPlayer.getCurrentPosition();
            Log.d("654321", "timePassed:" + timePassed);
            Log.d("654321", "??");
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //滚动歌词
                    mLrcView.seekLrcToTime(timePassed);
                }
            });

        }
    }


}
