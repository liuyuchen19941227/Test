package com.example.dllo.myapplication.detail.music_content;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseFragment;
import com.example.dllo.myapplication.base_class.MyApp;
import com.example.dllo.myapplication.main.SendMsgEventBean;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Fragment_Music_Page2_Detail extends BaseFragment{


    private TextView tv_song;
    private TextView tv_author;
    private ImageView iv_music_page2_img;
    private ImageView iv_music_page2_background;
    private ImageAsyncTask asyncTask;

    @Override
    protected int setLayout() {
        return R.layout.fragment_music_page2_detail;
    }

    @Override
    protected void initView() {
        iv_music_page2_img = bindView(R.id.iv_music_page2_img);
        iv_music_page2_background = bindView(R.id.iv_music_page2_background);
        tv_song = bindView(R.id.tv_music_page2_name);
        tv_author = bindView(R.id.tv_music_page2_author);
    }


    @Override
    protected void initData() {

        EventBus.getDefault().register(this);

        Intent intent = getActivity().getIntent();
        String song = intent.getStringExtra("song");
        String author = intent.getStringExtra("author");
        String img = intent.getStringExtra("img");



        tv_song.setText(song);
        tv_author.setText(author);
        if (!img.equals("")) {
            Picasso.with(getContext()).load(img).into(iv_music_page2_img);
        }
        asyncTask = new ImageAsyncTask();
        asyncTask.execute(img);

    }


    // EventBus接收数据,获取到传的值,添加了下面这句话,这个方法一定会执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ReceiveEvent(SendMsgEventBean sendMsgEvent) {
        // 歌曲的详细信息
        tv_song.setText(sendMsgEvent.getSong());
        tv_author.setText(sendMsgEvent.getAuthor());
        if (sendMsgEvent.getImg() != null){
            Picasso.with(getContext()).load(sendMsgEvent.getImg()).into(iv_music_page2_img);
        }
        // 这里...还是new一个吧...
        ImageAsyncTask asyncTask1 = new ImageAsyncTask();
        asyncTask1.execute(sendMsgEvent.getImg());
    }





    // 加载虚化的背景图片
    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("ImageAsyncTask", strings[0]);
                    InputStream stream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                }
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            BitmapDrawable drawable = new BitmapDrawable(changeBackgroundImage(bitmap));
            iv_music_page2_background.setBackground(drawable);
        }
    }


    // 图片虚化
    public Bitmap changeBackgroundImage(Bitmap sentBitmap) {
        if (sentBitmap != null) {
            if (Build.VERSION.SDK_INT > 16) {
                Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
                final RenderScript rs = RenderScript.create(MyApp.getContext());
                final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
                final Allocation output = Allocation.createTyped(rs, input.getType());
                final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
                script.setRadius(20.0f);
                script.setInput(input);
                script.forEach(output);
                output.copyTo(bitmap);
                return bitmap;
            }
        }
        return null;
    }






}
