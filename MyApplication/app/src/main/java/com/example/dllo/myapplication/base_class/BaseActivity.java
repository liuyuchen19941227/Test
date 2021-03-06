package com.example.dllo.myapplication.base_class;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import cn.jpush.android.api.JPushInterface;

// Activity

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayout());
        initView();
        initData();

    }

    // jniLibs


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        Log.d("MainActivity", "1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        Log.d("MainActivity", "2");
    }






    /**
     * 设置布局
     * @return 布局文件的id
     */
    protected  abstract int setLayout();

    /**
     * 初始化View 执行FindViewById操作
     */
     protected abstract void initView();

    /**
     * 初始化数据,例如拉取网络数据,或者一些逻辑代码
     */
    protected abstract void initData();


    /**
     * 简化FIndViewById的操作,不需要强转
     * @param id 组件的id
     * @param <T> T是所有View的子类,自动转化成对应的类型
     * @return
     */
    protected <T extends View> T bindView(int id) {
        return (T) findViewById(id);
    }




}
