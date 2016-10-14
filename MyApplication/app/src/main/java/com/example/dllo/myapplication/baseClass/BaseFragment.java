package com.example.dllo.myapplication.baseClass;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{

    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    // getContext();

    // Java代码不要有小写字母+下划线
    // 全大写才需要下划线


    // 布局初始化
    protected abstract int setLayout();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setLayout(), container, false);
    }


    // 控件初始化
    protected abstract void initView();
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }



    // 控件初始化时被调用
    protected <T extends View> T bindView(int id) {
        return (T) getView().findViewById(id);
    }

    protected <T extends View> T bindView(int id, View v) {
        return (T) v.findViewById(id);
    }


    // 添加活动
    protected abstract void initData();
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }




}
