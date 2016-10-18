package com.example.dllo.myapplication.base_class;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class MyApp extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;


        // 推送
        // 拷贝到app2
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush




        // ImageLoader
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
        ImageLoaderConfiguration configuration =
                new ImageLoaderConfiguration
                        .Builder(this)
                        .threadPoolSize(3).diskCacheFileCount(100)
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        .diskCache(new UnlimitedDiscCache(cacheDir))
                        .memoryCache(new WeakMemoryCache())
                        .build();
        ImageLoader.getInstance().init(configuration);


    }

    public static Context getContext() {
        return mContext;
    }



}
