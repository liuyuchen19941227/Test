package com.example.dllo.myapplication.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

// 轮播图(没有用到,用的是banner..)
public class CFPageAdapter extends PagerAdapter{

    private Context mContext;
    private ArrayList<ImageView> arrayList;

    // Context 没什么用啊
    public CFPageAdapter(Context mContext, ArrayList<ImageView> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // 去掉上一张图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
        container.removeView(arrayList.get(position));
    }


    // 获取下一张图
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(arrayList.get(position));
        return arrayList.get(position);
    }



}
