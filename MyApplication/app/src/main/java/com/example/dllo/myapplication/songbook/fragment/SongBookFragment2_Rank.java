package com.example.dllo.myapplication.songbook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseFragment;
import com.example.dllo.myapplication.base_class.GsonRequest;
import com.example.dllo.myapplication.base_class.MyApp;
import com.example.dllo.myapplication.base_class.VolleySingleton;
import com.example.dllo.myapplication.songbook.adapter.SongBook2_Rank_BaseAdapter;
import com.example.dllo.myapplication.songbook.bean.SongBook2_RankBean;
import com.example.dllo.myapplication.songbook.songbookdetails.DetailList2_Rank;
import com.example.dllo.myapplication.songbook.tools.URLValues;

import java.util.ArrayList;




public class SongBookFragment2_Rank extends BaseFragment{


    private ListView lv;
    private Context mContext;
    private ArrayList<SongBook2_RankBean> beans;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main2_songbook2_rank;
    }

    @Override
    protected void initView() {
        lv = bindView(R.id.lv_songbook2_rank);



    }

    @Override
    protected void initData() {

        // 找数据和安布局





        beans = new ArrayList<>();

        sendGet();


//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                String web = beans.get(0).getContent().get(i).getWeb_url();// 是空的啊....
////                Toast.makeText(mContext, "i:" + i, Toast.LENGTH_SHORT).show();
//                Log.d("SongBookFragment2_Rank", "2");
//
//            }
//        });







    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }




    private void sendGet() {

        // 封装好的新方法
        GsonRequest<SongBook2_RankBean> gsonRequest = new GsonRequest<SongBook2_RankBean>(
                URLValues.MUSICSTORE_TOP,
                SongBook2_RankBean.class,
                new Response.Listener<SongBook2_RankBean>() {
                    @Override
                    public void onResponse(SongBook2_RankBean response) {
                        beans.add(response);
                        // 线程方法(必须要写在线程中?)
                        SongBook2_Rank_BaseAdapter adapter = new SongBook2_Rank_BaseAdapter(MyApp.getContext());
                        //  getContext()动态获取的方法,点快了有时会获取不到  用onAttach()更好一些
                        adapter.setArrayList(beans);
                        lv.setAdapter(adapter);



                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                String url = "" + beans.get(0).getContent().get(i).getType();
                                url = URLValues.TOP_SONG_FRONT + url + URLValues.TOP_SONG_BEHIND;
                                Log.d("SongBookFragment2_Rank", url);


//                                Intent intent = new Intent(MyApp.getContext(), DetailList2_Rank.class);
//                                intent.putExtra("url", url);
//                                startActivity(intent);
//                                getActivity().overridePendingTransition(R.anim.list_detail_enter, R.anim.list_detail_exit);



                                DetailList2_Rank fragment = new DetailList2_Rank();

//                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                                transaction.replace(R.id.fl_main, fragment);
//                                transaction.addToBackStack(null);
//                                transaction.commit();


                                // 发送数据
                                Bundle bundle = new Bundle();
                                bundle.putString("url", url);
                                fragment.setArguments(bundle);



                                // EventBus必须要注册的界面先运行,然后这边再发送
                                // SendMsgEventBean event = new SendMsgEventBean();
                                // event.setUrl(url);
                                // EventBus.getDefault().post(event);



                                // 界面切换并添加动画效果
                                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.list_detail_enter, R.anim.list_detail_exit).addToBackStack(null).replace(R.id.fl_main, fragment).commit();



                            }
                        });



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SongBookFragment2_Rank", "error");
                    }
                });
        // 这句话必须要有,不然无法解析数据
        VolleySingleton.getInstance().addRequest(gsonRequest);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }





}
