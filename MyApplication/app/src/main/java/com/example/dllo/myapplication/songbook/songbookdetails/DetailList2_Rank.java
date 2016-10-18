package com.example.dllo.myapplication.songbook.songbookdetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.BaseFragment;
import com.example.dllo.myapplication.base_class.DividerItemDecoration;
import com.example.dllo.myapplication.base_class.GsonRequest;
import com.example.dllo.myapplication.base_class.MyApp;
import com.example.dllo.myapplication.base_class.VolleySingleton;
import com.example.dllo.myapplication.detail.musicplay.MusicService;
import com.example.dllo.myapplication.songbook.tools.OnRecyclerViewItemListener;
import com.example.dllo.myapplication.songbook.tools.URLValues;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class DetailList2_Rank extends BaseFragment{

    private RecyclerView rv;
    private String url;
    private TextView tv;

    @Override
    protected int setLayout() {
        return R.layout.fragment_list2_detail_rank;
    }

    @Override
    protected void initView() {

        rv = bindView(R.id.rv_list2_detail_music_list);
        AppBarLayout appBarLayout = bindView(R.id.appbar2);
        tv = bindView(R.id.tv_list_detail_update_date);


        // 滑动隐藏/显示上方控件
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / maxScroll;
                if (percentage > 0.8f) {
                    tv.setVisibility(View.INVISIBLE);
                } else {
                    tv.setVisibility(View.VISIBLE);
                }
            }
        });


        ImageView iv = bindView(R.id.iv_back2);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DetailList2_Rank", "destroy");

                getFragmentManager().beginTransaction().setCustomAnimations( R.anim.list_detail_back_enter, R.anim.list_detail_back_exit).remove(DetailList2_Rank.this).commit();


            }
        });


        // 分享按钮
        ImageView iv_share = bindView(R.id.iv_list2_detail_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareSDK.initSDK(getContext(),"sharesdk的appkey");
                showShare();
            }
        });



    }

    @Override
    protected void initData(){

//        Intent intent = getActivity().getIntent();
//        url = intent.getStringExtra("url");
//        Log.d("DetailList2_Rank", url);


        Bundle bundle = getArguments();
        url = bundle.getString("url");

        sendGet();


        // 这个页面必须要先运行,从第二个页面往回传才可以用EventBus!
        // EventBus.getDefault().register(this);

    }


    // EventBus接收数据,获取到传的值
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void ReceiveEvent(SendMsgEventBean sendMsgEvent) {
//        url = sendMsgEvent.getUrl();
//        Log.d("DetailList2_Rank2", url);
//        sendGet();
//    }



    // 分享
    public void showShare() {
        ShareSDK.initSDK(getContext());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(getContext());
    }




    private void sendGet() {

        GsonRequest<DetailList2_Rank_Bean> request = new GsonRequest<DetailList2_Rank_Bean>(
                url,
                DetailList2_Rank_Bean.class,
                new Response.Listener<DetailList2_Rank_Bean>() {

                    @Override
                    public void onResponse(DetailList2_Rank_Bean response) {

                        Log.d("DetailList2_Rank", url);

                        final ArrayList<DetailList2_Rank_Bean> arrayList = new ArrayList<>();
                        arrayList.add(response);

                        Log.d("DetailList2_Rank", arrayList.get(0).getSong_list().get(2).getTitle());

                        DetailList2_Rank_Adapter adapter = new DetailList2_Rank_Adapter(MyApp.getContext());
                        adapter.setArrayList(arrayList);

                        rv.setAdapter(adapter);

                        LinearLayoutManager manager = new LinearLayoutManager(MyApp.getContext());
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        rv.setLayoutManager(manager);
                        // 分割线
                        rv.addItemDecoration(new DividerItemDecoration(MyApp.getContext(), DividerItemDecoration.VERTICAL_LIST));



                        Log.d("DetailList2_Rank", response.getBillboard().getName());

                        // 标题
                        TextView title = bindView(R.id.tv_list_detail_title2);
                        title.setText(response.getBillboard().getName());

                        // 更新日期
                        TextView update = bindView(R.id.tv_list_detail_update_date);
                        update.setText("更新日期:" + response.getBillboard().getUpdate_date());

                        // 歌曲数目
                        TextView musicCount= bindView(R.id.tv_list_detail_music_count);
                        musicCount.setText(response.getSong_list().size() + "首歌");


                        // 主线程禁止做耗时操作
                        // 在子线程实现网络请求和图片赋值
                        ImgAsync imgAsync = new ImgAsync();
                        imgAsync.execute(response.getBillboard().getPic_s210());


                        adapter.onItemClickListener(new OnRecyclerViewItemListener() {
                            @Override
                            public void onItemClick(View view, String url, int position) {
//                                Intent intent = new Intent(DetailList2_Rank.this, MusicActivity.class);
//                                intent.putExtra("url", url);
//                                startActivity(intent);
//                                getActivity().overridePendingTransition(R.anim.list_detail_enter, R.anim.list_detail_exit);


                                ArrayList<String> arrayList1 = new ArrayList<>();
                                for (int i = 0; i < arrayList.get(0).getSong_list().size(); i++) {
                                    String urlLittle = URLValues.PLAY_FRONT + arrayList.get(0).getSong_list().get(i).getSong_id() + URLValues.PLAY_BEHIND;
                                    arrayList1.add(urlLittle);
                                    Log.d("DetailList1_Recommend1_", urlLittle);
                                }



                                Intent intent = new Intent(MyApp.getContext(), MusicService.class);
                                intent.putExtra("url", url);
                                intent.putExtra("position", position);
                                intent.putExtra("urlMore", arrayList1);
                                getActivity().startService(intent);



                            }


                        });

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DetailList2_Rank", "error");
                    }
                }
        );

        VolleySingleton.getInstance().addRequest(request);

    }


    private class ImgAsync extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            String str = strings[0];
            // 图片
            try {
                URL url = new URL(str);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream stream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                }
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            AppBarLayout appBarLayout = bindView(R.id.appbar2);
            appBarLayout.setBackground(bitmapDrawable);
        }
    }




}
