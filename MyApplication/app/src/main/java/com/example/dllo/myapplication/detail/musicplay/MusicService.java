package com.example.dllo.myapplication.detail.musicplay;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.example.dllo.myapplication.base_class.VolleySingleton;
import com.example.dllo.myapplication.detail.music_content.MusicValues;
import com.example.dllo.myapplication.detail.popup_window.MusicItemBean;
import com.example.dllo.myapplication.main.SendMsgEventBean;
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
//    private MusicBean bean;
    private ArrayList<String> arrayList;
    private MusicBean musicBean;

    private Intent intent1;
    private int totalTime;
    private String lrc;// 歌词链接


    private  Gson gson;
    private SendMsgEventBean event;
    private MyMusicBroadCast broadCast;
    private IntentFilter filter;

    // 播放模式
    private int playModel;
    private int musicCurrentPosition;
    private ArrayList<MusicItemBean> musicItemBeans;
    private Notification notification;
    private String url;
    private String link;

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
        // EventBus
        event = new SendMsgEventBean();


        // 注册Notification
        broadCast = new MyMusicBroadCast();
        filter = new IntentFilter();
        filter.addAction(getPackageName() + "music");
        filter.addAction(getPackageName() + "previousMusic");
        filter.addAction(getPackageName() + "nextMusic");
        registerReceiver(broadCast, filter);


        // 播放完毕的监听
        musicPlayer.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextMusic();
            }
        });

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            // 整个歌单的链接(未解析)
            arrayList = intent.getStringArrayListExtra("urlMore");

            // 当前播放位置 默认是0
            position = intent.getIntExtra("position", 0);
            Log.d("MusicService", "position:" + position);

            // 正在播放的音乐
            url = intent.getStringExtra("url");
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

        // 获取总的播放时长
        public int getMusicTotalDuration(){
            if (musicPlayer.player != null){
                return musicPlayer.player.getDuration();
            }
            return -1;
        }

        // 获取当前的播放位置
        public int getMusicCurrentP(){
            if (musicPlayer.player != null){
                return musicPlayer.player.getCurrentPosition();
            }
            return -1;
        }

        public String getUrl() {
            return url;
        }

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
            Log.d(TAG, "暂停");
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


        public void setPlayModel(int model) {
            playModel = model;
        }

        public String getLrcLlink() {
            return lrc;
        }



        public void sendBroadCast() {
            // 控制进度条的广播
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        musicCurrentPosition = musicPlayer.player.getCurrentPosition();
                        // 发广播
                        intent1.putExtra("pro", musicCurrentPosition);
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

        public MusicBean getMusicBeanAll() {
            return musicBean;
        }

    }



    // 单曲解析
    private void sendGet(final String url) {

        // 换成StringRequest 获取那些数据,然后减掉
        Log.d(TAG, url);
        StringRequest request2 = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        response = response.substring(1, response.length() - 2);
                        Log.d(TAG, response);
                        musicBean = gson.fromJson(response, MusicBean.class);
                        // 音乐链接
                        link = musicBean.getBitrate().getFile_link();
                        Log.d("MusicService", link);
                        // 控制播放
                        musicPlayer.playMusic(link);
                        // EventBus发送数据
                        sendMsgEventBus();
                        // 持久化技术
                        saveSP();
                        // 显示提示
                        showMusicNotify();
                        // 获取总时长
                        totalTime = musicPlayer.player.getDuration();
                        // 歌词
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
        binder.sendBroadCast();
    }



    public void sendMsgEventBus(){

        // EventBus必须要注册的界面先加载,然后这边再发送
        event.setAuthor(musicBean.getSonginfo().getAuthor());
        event.setSong(musicBean.getSonginfo().getTitle());
        event.setImg(musicBean.getSonginfo().getPic_radio());
        event.setLrclink(musicBean.getSonginfo().getLrclink());// 歌词链接
        event.setUrl(link);
        event.setMusics(arrayList);// 将整个歌单传过去
        // event.setMusicBean(musicBean);

        musicItemBeans = new ArrayList<>();
        if (arrayList != null) {
            // 歌单的url
            for (int i = 0; i < arrayList.size(); i++) {
                // VolleyApplication等都没有分好
                sendGetArr(arrayList.get(i));
                musicItemBeans.get(i).setIsPlay(0);
            }
        }

        // 将正在播放位置的歌曲isPlay属性置为1
        musicItemBeans.get(position).setIsPlay(1);



        // 传输解析好了的歌单信息
        event.setMusicItemBeanArrayList(musicItemBeans);

        EventBus.getDefault().post(event);

    }



    public void saveSP(){

        // 数据持久化技术
        SharedPreferences msgSetSp = getSharedPreferences("music", MODE_PRIVATE);
        SharedPreferences.Editor editor = msgSetSp.edit();
        editor.putString("song", musicBean.getSonginfo().getTitle());
        editor.putString("author", musicBean.getSonginfo().getAuthor());
        editor.putString("img", musicBean.getSonginfo().getPic_radio());
        editor.putString("songUrl", musicBean.getBitrate().getFile_link());
        editor.putString("lrcLink", musicBean.getSonginfo().getLrclink());
        editor.putInt("playMode", playModel);
        editor.putInt("totalTime", binder.getMusicTotalDuration());// 总时长
        editor.commit();

    }





    // 整个播放列表(解析后的)
    private void sendGetArr(String s) {
        // 用这个方法的话,bean还是要加上的

        final MusicItemBean musicItemBean = new MusicItemBean();

        StringRequest request2 = new StringRequest(
                s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String responseNew = response.substring(1, response.length() - 2);
                        final Gson gson = new Gson();

                        MusicBean bean = gson.fromJson(responseNew, MusicBean.class);
                        musicItemBean.setSong(bean.getSonginfo().getTitle());
                        musicItemBean.setAuthor(bean.getSonginfo().getAuthor());
                        musicItemBean.setUrl(bean.getBitrate().getFile_link());
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

        musicItemBeans.add(musicItemBean);

    }




    // 播放模式
    // 可以在这里面判断播放完的下一步
    public void nextMusic() {

        int result = playModel % 4;

        if (result == MusicValues.PLAYMODE_LOOPPLAY) {
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
            Log.d(TAG, "0:" + 0);
        } else if (result == MusicValues.PLAYMODE_ORDERPLAY) {
            // 顺序播放
            if (arrayList != null) {
                if (position == (arrayList.size() - 1)) {
                    // 直接停止
                    musicPlayer.player.stop();
                } else {
                    // 单独找到那首歌
                    sendGet(arrayList.get(++position));
                }
            } else {
                Toast.makeText(this, "歌单为空,没有下一曲!!", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "1:" + 1);
        } else if (result == MusicValues.PLAYMODE_RANDOMPLAY) {
            // 随机播放
            if (arrayList != null) {
                position = (int) (Math.random() * arrayList.size());
                sendGet(arrayList.get(position));
            }
            Log.d(TAG, "2:" + 2);
        } else if (result == MusicValues.PLAYMODE_SINGLEPLAY) {
            sendGet(arrayList.get(position));
            Log.d(TAG, "3:" + 3);
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

//            // 注册Notification
//            broadCast = new MyMusicBroadCast();
//            filter = new IntentFilter();
//            filter.addAction(getPackageName() + "music");
//            filter.addAction(getPackageName() + "previousMusic");
//            filter.addAction(getPackageName() + "nextMusic");
//            registerReceiver(broadCast, filter);



            String type = intent.getAction();
            if (type.equals(getPackageName() + "music")) {
                if (musicPlayer.isPlay()) {
                    musicPlayer.pause();
                } else {
                    musicPlayer.start();
                }
                Log.d(TAG, "播放/暂停");
            } else if (type.equals(getPackageName() + "previousMusic")){
                Log.d(TAG, "上一曲");
                previousMusic();
            } else if (type.equals(getPackageName() + "nextMusic")) {
                nextMusic();
                Log.d(TAG, "下一曲");
            }






        }
    }


    // 顶部弹出的通知栏
    private void showMusicNotify() {

        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
                if (notification != null) {
                    // 第一次运行,notification是空的....可是为什么仍然有图片??
                    manager.notify(350, notification);
                }
            }
        });


        // remoteViews.setImageViewResource(R.id.iv_custom_music, R.mipmap.icon);
        remoteViews.setTextViewText(R.id.tv_custom_music, musicBean.getSonginfo().getTitle());
        remoteViews.setTextViewText(R.id.tv_custom_singer, musicBean.getSonginfo().getAuthor());


        // 设定通知栏内的按钮监听 发送广播
        Intent intent = new Intent(getPackageName() + "music");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 102, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_notify_start, pendingIntent);

        Intent intentPrevious = new Intent(getPackageName() + "previousMusic");
        PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(this, 103, intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_notify_previous, pendingIntentPrevious);

        Intent intentNext = new Intent(getPackageName() + "nextMusic");
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this, 104, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_notify_next, pendingIntentNext);





        builder.setContent(remoteViews);


        notification = builder.build();
        // 点击后自动消失
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        manager.notify(350, notification);


    }


}
