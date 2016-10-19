package com.example.dllo.myapplication.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncTaskPicActivity extends BaseActivity{


    private ImageView iv;
    private TextView tv;

    @Override
    protected int setLayout() {
        return R.layout.async_task_pic;
    }

    @Override
    protected void initView() {
        iv = bindView(R.id.iv_async_task_pic);

        tv = bindView(R.id.tv_async_task_count_down);

    }

    @Override
    protected void initData() {

//        final AsyncTask asyncImg = new AsyncTaskPic();
//        asyncImg.execute("http://img.zcool.cn/community/015664554b47fb000001bf72994554.jpg");



        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.welcome_pic);
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        iv.setBackground(drawable);


        tv.setText("跳过");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AsyncTaskPicActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });



        // 子线程不能刷新UI


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(AsyncTaskPicActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        thread.start();


    }


    public class AsyncTaskPic extends AsyncTask<String , Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            String str = strings[0];
            try {
                URL url = new URL(str);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
                httpURLConnection.disconnect();
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

            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            iv.setBackground(drawable);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(AsyncTaskPicActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }).start();

        }
    }






}
