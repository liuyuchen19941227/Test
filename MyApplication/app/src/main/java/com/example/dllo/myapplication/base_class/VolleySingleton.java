package com.example.dllo.myapplication.base_class;

import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.cache.MemoryCache;

// 单例模式
public class VolleySingleton {

    private static VolleySingleton mVolleySingleton;

    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApp.getContext());
        imageLoader = new ImageLoader(mRequestQueue, new MemoryCache());
    }

    public static VolleySingleton getInstance() {
        if (mVolleySingleton == null) {
            synchronized (VolleySingleton.class) {
                if (mVolleySingleton == null) {
                    mVolleySingleton = new VolleySingleton();
                }
            }
        }
        return mVolleySingleton;
    }


    public void getImage(String imgUrl, ImageView imageView){
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl, imageListener);
    }


    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }


    public void addRequest(Request request) {
        mRequestQueue.add(request);
    }








}
