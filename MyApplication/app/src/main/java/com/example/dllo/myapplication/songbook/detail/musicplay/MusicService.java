package com.example.dllo.myapplication.songbook.detail.musicplay;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.baseClass.VolleySingleton;
import com.example.dllo.myapplication.songbook.fragment.SendMsgEventBean;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class MusicService extends Service {


    private static final String TAG = "TAG_MusicService";
    private MusicPlayer musicPlayer;
    private MyBinder binder;

    private int position;
    private MusicBean bean;
    private ArrayList<String> arrayList;
    private MusicBean musicBean;

    private Intent intent1;
    private int totalTime;
    private String lrc;// 歌词链接


    private  Gson gson;
    private SendMsgEventBean event;
    private MyMusicBroadCast broadCast;
    private IntentFilter filter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("lkjh", "新一曲3");
        musicPlayer = new MusicPlayer();
        binder = new MyBinder();
        intent1 = new Intent("progress");
        gson = new Gson();
        event = new SendMsgEventBean();
        // 注册广播
        broadCast = new MyMusicBroadCast();
        filter = new IntentFilter();
        // Notify的按钮点击广播
        filter.addAction(getPackageName() + "music");
        registerReceiver(broadCast, filter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 耗时操作
        if (intent != null) {
            // 整个歌单的链接(未解析)
            arrayList = intent.getStringArrayListExtra("urlMore");
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    Log.d("MusicService", arrayList.get(i));
                }
            }
            // 当前播放位置 默认是0
            position = intent.getIntExtra("position", 0);
            Log.d("MusicService", "position:" + position);

            // 正在播放的音乐
            final String url = intent.getStringExtra("url");
            if (url != null) {
                sendGet(url);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁掉之前的服务
        musicPlayer.player.release();
    }


    // 用来传数据的,针对binder去操作
    public class MyBinder extends Binder {


        public MyBinder() {
        }

        public MusicPlayer getMusicPlayer() {
            return musicPlayer;
        }


        public void next() {
            nextMusic();
        }

        public void previous() {
            previousMusic();
        }

        public void pause() {
            musicPlayer.player.pause();
        }

        public void play() {
            musicPlayer.player.start();
        }

        public boolean isPlay() {
            if (musicPlayer.player.isPlaying()) {
                return true;
            } else {
                return false;
            }
        }


        public int getTime() {
            return musicPlayer.getTotalTime();
        }


    }


    // style风格封装 字体颜色统一


    // 单曲解析
    private void sendGet(final String url) {

        // 换成StringRequest 获取那些数据,然后减掉


        StringRequest request2 = new StringRequest(
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        response = response.substring(1, response.length() - 2);

                        musicBean = gson.fromJson(response, MusicBean.class);
                        // 音乐链接
                        String link = musicBean.getBitrate().getFile_link();
                        Log.d("MusicService", link);


                        // EventBus必须要注册的界面先加载,然后这边再发送
                        event.setAuthor(musicBean.getSonginfo().getAuthor());
                        event.setSong(musicBean.getSonginfo().getTitle());
                        event.setImg(musicBean.getSonginfo().getPic_radio());
                        event.setLrclink(musicBean.getSonginfo().getLrclink());// 歌词链接
                        event.setUrl(link);
                        EventBus.getDefault().post(event);// 此位置加断点,这句话不会执行


                        // 控制播放
                        musicPlayer.playMusic(link);


                        // 监听自然播放完毕,自动切歌
                        musicPlayer.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                nextMusic();
                            }
                        });


                        // 显示提示
                        showMusicNotify();






                        totalTime = musicPlayer.player.getDuration();
                        lrc = musicBean.getSonginfo().getLrclink();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MusicActivity", "error");
                    }
                }
        );
        VolleySingleton.getInstance().addRequest(request2);



        // 控制进度条的广播
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int pos = musicPlayer.player.getCurrentPosition();
                    // 发广播
                    intent1.putExtra("pro", pos);
                    intent1.putExtra("lrclink", lrc);// 歌词
                    intent1.putExtra("totalTime", totalTime);
                    sendBroadcast(intent1);
                    // 剪短0.5秒
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }


    // 整个播放列表(解析后的)
    private void sendGetArr(String s) {
        StringRequest request2 = new StringRequest(
                s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.substring(1, response.length() - 2);
                        final Gson gson = new Gson();
                        bean = gson.fromJson(response, MusicBean.class);
                        ArrayList<String> musicList = new ArrayList<>();
                        // 音乐链接
                        String link = bean.getBitrate().getFile_link();
                        musicList.add(link);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MusicActivity", "error");
                    }
                }
        );
        VolleySingleton.getInstance().addRequest(request2);
    }


    public void nextMusic() {
        // 如果正好放到最后一首歌了
        // 在这也要用EventBus发送数据到MainActivity更新UI
        if (arrayList != null) {
            if (position == (arrayList.size() - 1)) {
                sendGet(arrayList.get(0));
                position = 0;
            } else {
                // 单独找到那首歌
                sendGet(arrayList.get(++position));
            }
        } else {
            Toast.makeText(this, "歌单为空,没有下一曲!!", Toast.LENGTH_SHORT).show();
        }
    }


    // 播放上一曲
    public void previousMusic() {
        if (arrayList != null) {
            if (position == 0) {
                // 如果正在播放第一首,那就切换为最后一首
                sendGet(arrayList.get(arrayList.size() - 1));
                position = arrayList.size() - 1;
            } else {
                sendGet(arrayList.get(--position));
            }
        } else {
            Toast.makeText(this, "上一曲为空", Toast.LENGTH_SHORT).show();
        }
    }


    // 广播接收器,暂停/播放
    private class MyMusicBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MyMusicBroadCast", "发送广播");
            if (musicPlayer.isPlay()) {
                musicPlayer.pause();
            } else {
                musicPlayer.start();
            }
        }
    }


    // 顶部弹出的通知栏
    private void showMusicNotify() {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.icon);
        builder.setTicker("音乐开始播放");


        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_custom_music);
        // 使用第三方给图
        ImageLoader.getInstance().loadImage(musicBean.getSonginfo().getPic_radio(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                remoteViews.setImageViewBitmap(R.id.iv_custom_music, loadedImage);
            }
        });


        //        remoteViews.setImageViewResource(R.id.iv_custom_music, R.mipmap.icon);
        remoteViews.setTextViewText(R.id.tv_custom_music, musicBean.getSonginfo().getTitle());
        remoteViews.setTextViewText(R.id.tv_custom_singer, musicBean.getSonginfo().getAuthor());


        // 设定通知栏内的按钮监听 发送广播
        Intent intent = new Intent(getPackageName() + "music");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 102, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_notify_start, pendingIntent);
        builder.setContent(remoteViews);


        Notification notification = builder.build();
        // 点击后自动消失
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(350, notification);


    }


}
