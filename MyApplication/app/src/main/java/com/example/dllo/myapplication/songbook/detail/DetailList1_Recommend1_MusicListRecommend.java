package com.example.dllo.myapplication.songbook.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.baseClass.BaseFragment;
import com.example.dllo.myapplication.baseClass.DividerItemDecoration;
import com.example.dllo.myapplication.baseClass.GsonRequest;
import com.example.dllo.myapplication.baseClass.MyApp;
import com.example.dllo.myapplication.baseClass.VolleySingleton;
import com.example.dllo.myapplication.songbook.detail.musicplay.MusicService;
import com.example.dllo.myapplication.songbook.tools.OnRecyclerViewItemListener;
import com.example.dllo.myapplication.songbook.tools.URLValues;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailList1_Recommend1_MusicListRecommend extends BaseFragment {


    private RecyclerView rv;
    private String url;
    private AppBarLayout appBarLayout;
    private TextView title;
    private Bitmap bitmap;
    private LinearLayout ll;
    private RelativeLayout rl;
    private ArrayList<DetailList1_Recommend1_Bean> arrayList;


    @Override
    protected int setLayout() {
        return R.layout.fragment_list1_detail1_recommendlist;
    }

    @Override
    protected void initView() {

        // 找布局,绑定数据和添加点击事件
        // recyclerView
        rv = bindView(R.id.rv_list_detail_music_list);
        appBarLayout = bindView(R.id.appbar);

        ll = bindView(R.id.ll_three);
        rl = bindView(R.id.rl_two);


        ImageView iv = bindView(R.id.iv_back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove移出屏幕
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.list_detail_back_enter, R.anim.list_detail_back_exit).remove(DetailList1_Recommend1_MusicListRecommend.this).commit();
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initData() {


        // 协调布局隐藏部分控件
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                if (percentage > 0.8f) {
                    ll.setVisibility(View.INVISIBLE);
                    rl.setVisibility(View.INVISIBLE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                    rl.setVisibility(View.VISIBLE);
                }
            }
        });

//        Intent intent = getActivity().getIntent();
//        url = intent.getStringExtra("url");


        // 获取数据
        Bundle bundle = getArguments();
        url = bundle.getString("url");
        Log.d("DetailList1_Recommend1_", url);


        // 这个必须要放在最下面.....
        sendGet();

    }


    private void sendGet() {


        final GsonRequest<DetailList1_Recommend1_Bean> request = new GsonRequest<DetailList1_Recommend1_Bean>(
                url,
                DetailList1_Recommend1_Bean.class,
                new Response.Listener<DetailList1_Recommend1_Bean>() {
                    @Override
                    public void onResponse(DetailList1_Recommend1_Bean response) {


                        Log.d("DetailList11", url);
                        arrayList = new ArrayList<>();
                        arrayList.add(response);


                        DetailList1_Recommend1_Adapter adapter = new DetailList1_Recommend1_Adapter(MyApp.getContext());
                        adapter.setArrayList(arrayList);
                        LinearLayoutManager manager = new LinearLayoutManager(MyApp.getContext());
                        manager.setOrientation(LinearLayoutManager.VERTICAL);
                        rv.setLayoutManager(manager);
                        rv.setAdapter(adapter);

                        // 分割线
                        rv.addItemDecoration(new DividerItemDecoration(MyApp.getContext(), DividerItemDecoration.VERTICAL_LIST));





                        // 标题
                        TextView title =  bindView(R.id.tv_list_detail_title);
                        title.setText(response.getTitle());
                        // 音乐图片
                        ImageView img= bindView(R.id.iv_list_detail_song);
                        Picasso.with(MyApp.getContext()).load(response.getPic_300()).into(img);
                        // 标签
                        TextView lable = bindView(R.id.tv_list_detail_label);
                        lable.setText("标签:" + response.getTag());
                        lable.setTextSize(8);


                        // 主线程禁止做耗时操作
                        // 在子线程实现网络请求和图片赋值
                        ImgAsync imgAsync = new ImgAsync();
                        imgAsync.execute(response.getPic_300());

                        // 作者
                        TextView author = bindView(R.id.tv_list_detail_author);
                        // 作者头像
                        ImageView authorImg = bindView(R.id.iv_list_detail_author);

                        // 播放量
                        TextView playCount = bindView(R.id.tv_list_detail_play_count);
                        playCount.setText("播放量:" + response.getListenum());

                        // 收藏量
                        TextView collectCount = bindView(R.id.tv_list_detail_collection_count);
                        collectCount.setText("收藏量:" + response.getCollectnum());

                        // 评论数
                        TextView commentCount = bindView(R.id.tv_list_detail_comment_count);
                        // 转发数
                        TextView shareCount = bindView(R.id.tv_list_detail_share_count);

                        // 歌曲数目
                        TextView musicCount = bindView(R.id.tv_list_detail_music_count);
                        musicCount.setText(response.getContent().size() + "首歌");




                        // 点击的方法
                        adapter.setOnItemListener(new OnRecyclerViewItemListener() {

                            @Override
                            public void onItemClick(View view, String url, int position) {

                                ArrayList<String> arrayList1 = new ArrayList<String>();
                                for (int i = 0; i < arrayList.get(0).getContent().size(); i++) {
                                    String urlLittle = URLValues.PLAY_FRONT + arrayList.get(0).getContent().get(i).getSong_id() + URLValues.PLAY_BEHIND;
                                    arrayList1.add(urlLittle);
                                    Log.d("DetailList1_Recommend1_", urlLittle);
                                }


                                // 交给服务去做 服务的机制,只有一个 new的是intent
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
                        Log.d("DetailList1_Recommend1", "error");
                    }
                }
        );

        VolleySingleton.getInstance().addRequest(request);


    }








    // 图片虚化
    public Bitmap changeBackgroundImage(Bitmap sentBitmap) {
        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript rs = RenderScript.create(MyApp.getContext());
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(20.0f);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        return null;
    }



    // 设置背景图片
    private class ImgAsync extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            String str = strings[0];
            try {
                URL url = new URL(str);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
                httpURLConnection.disconnect();
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
            // Bitmap->Drawable 先虚化一下
            BitmapDrawable drawable = new BitmapDrawable(changeBackgroundImage(bitmap));
            // 整个布局的背景图片
            AppBarLayout appBarLayout = bindView(R.id.appbar);
            appBarLayout.setBackground(drawable);
        }
    }




}
