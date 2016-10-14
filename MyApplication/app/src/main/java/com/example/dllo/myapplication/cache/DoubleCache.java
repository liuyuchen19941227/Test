package com.example.dllo.myapplication.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

public class DoubleCache implements ImageLoader.ImageCache{

    private MemoryCache mMemoryCache;
    private DiskCache mDiskCache;

    public DoubleCache(Context context) {

        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);

    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap;
        bitmap = mMemoryCache.getBitmap(url);
        if (bitmap == null) {
            bitmap = mDiskCache.getBitmap(url);
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.putBitmap(url, bitmap);
        mDiskCache.putBitmap(url, bitmap);
    }
}
