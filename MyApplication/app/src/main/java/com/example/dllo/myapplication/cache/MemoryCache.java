package com.example.dllo.myapplication.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class MemoryCache implements ImageLoader.ImageCache{

    private LruCache<String, Bitmap> mLruCache;


    public MemoryCache() {
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
        mLruCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mLruCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }
}
