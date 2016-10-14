package com.example.dllo.myapplication.songbook.fragment;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.adapter.FragmentAdapter;
import com.example.dllo.myapplication.baseClass.BaseFragment;

import java.util.ArrayList;

public class SongBookFragment extends BaseFragment{

    private TabLayout tb;
    private ViewPager vp;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main2_songbook;
    }

    @Override
    protected void initView() {

        // 去找那几个Fragment
        tb = bindView(R.id.tb_songbook);
        vp = bindView(R.id.vp_songbook);

    }

    @Override
    protected void initData() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new SongBookFragment1_Recommend());
        fragments.add(new SongBookFragment3_MusicList());
        fragments.add(new SongBookFragment2_Rank());
//        fragments.add(new DetailList2_Rank());

        fragments.add(new SongBookFragment4_Radio());
        fragments.add(new SongBookFragment5_MV());

        FragmentAdapter adapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);
        tb.setupWithViewPager(vp);
        tb.setSelectedTabIndicatorColor(Color.BLUE);
        tb.setTabTextColors(Color.BLACK, Color.BLUE);


        tb.getTabAt(0).setText("推荐");
        tb.getTabAt(2).setText("歌单");
        tb.getTabAt(1).setText("榜单");
        tb.getTabAt(3).setText("视频");
        tb.getTabAt(4).setText("K歌");





    }



}
