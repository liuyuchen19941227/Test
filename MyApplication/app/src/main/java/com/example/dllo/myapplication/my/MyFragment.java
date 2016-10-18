package com.example.dllo.myapplication.my;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseFragment;
import com.example.dllo.myapplication.local_music.LocalMusicFragment;

public class MyFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int setLayout() {
        return R.layout.fragment_main1_my;


    }

    @Override
    protected void initView() {

        TextView tvLocalMusic = bindView(R.id.tv_fragment_main_my_query_music);
        tvLocalMusic.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_main_my_query_music:
                queryLocalMusic();
                break;
        }
    }

    private void queryLocalMusic() {

        Toast.makeText(mContext, "查询本地音乐", Toast.LENGTH_SHORT).show();

        // 用getActivity().解决
//        Intent intent = new Intent(getActivity(), LocalMusicFragment.class);
//        getActivity().startActivity(intent);


        // Fragment  跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_main_my, new LocalMusicFragment());
        transaction.addToBackStack(null);
        transaction.commit();



    }






}
