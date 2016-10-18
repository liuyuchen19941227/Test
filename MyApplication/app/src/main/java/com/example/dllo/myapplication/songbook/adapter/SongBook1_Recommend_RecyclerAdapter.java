package com.example.dllo.myapplication.songbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.dllo.myapplication.R;
import com.example.dllo.myapplication.base_class.MyApp;
import com.example.dllo.myapplication.songbook.bean.CarouselFigureBean;
import com.example.dllo.myapplication.songbook.tools.OnRecyclerViewItemListener;
import com.example.dllo.myapplication.songbook.tools.URLValues;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;

//import com.example.dllo.myapplication.cache.DoubleCache;


public class SongBook1_Recommend_RecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<CarouselFigureBean> arrayList;
    private Context mContext;
    private CarouselFigureBean bean;
    //    private int pos;


    private RequestQueue queue;
    private ImageLoader imageLoader;

    // 没有用上??
    private DisplayImageOptions options;


    private OnRecyclerViewItemListener listener;
    private int viewType;


    // 继承点击接口,不是自定义的接口

    public void setOnItemClickListener(OnRecyclerViewItemListener listener) {
        this.listener = listener;
    }


    public SongBook1_Recommend_RecyclerAdapter(Context mContext) {
        this.mContext = mContext;
        // 创建请求队列
        queue = Volley.newRequestQueue(mContext);
        // imageLoader = new ImageLoader(queue, new DoubleCache(mContext));
    }


    public void setArrayList(ArrayList<CarouselFigureBean> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }


    // bean = arrayList.get(0);
    public void setBean(CarouselFigureBean bean) {
        this.bean = bean;
        notifyDataSetChanged();
    }


    // 获取position编号
    @Override
    public int getItemViewType(int position) {
        // 不同的style进行分门别类1~15
        viewType = bean.getModule().get(position).getPos();// 点击的位置gv
        return viewType;
    }


    // 按照解析的数据顺序进行加载布局
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType) {
            // Rv不能用banner

            case 1:
                View view0 = LayoutInflater.from(mContext).inflate(R.layout.item_songbook1_recommend0_lunbo, parent, false);
                ViewHolder0 viewHolder0 = new ViewHolder0(view0);
                return viewHolder0;


            // 音乐导航(三张图)  entry
            case 2:
                View view1 = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.view_songbook1_recommend1_threeimg, parent, false);
                ViewHolder1 viewHolder1 = new ViewHolder1(view1, 2);
                return viewHolder1;


            // 歌单推荐  diy
            case 4:
                View view2 = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.view_songbook1_recommend2_listrecommend, parent, false);
                ViewHolder2 viewHolder2 = new ViewHolder2(view2, 4);
                return viewHolder2;


            // 新碟上架  mix_1
            case 6:
                View view2_1 = LayoutInflater.from(mContext).inflate(R.layout.view_songbook1_recommend2_listrecommend, parent, false);
                ViewHolder2 viewHolder2_1 = new ViewHolder2(view2_1, 6);
                return viewHolder2_1;

            // 热销专辑  mix_22
            case 7:
                View view2_2 = LayoutInflater.from(mContext).inflate(R.layout.view_songbook1_recommend2_listrecommend, parent, false);
                ViewHolder22 viewHolder2_2 = new ViewHolder22(view2_2, 7);
                return viewHolder2_2;


            // 商城固定入口(广告图片) mod_26
            case 8:
                View view6 = LayoutInflater.from(mContext).inflate(R.layout.item_songbook1_recommend6_advertisement, parent, false);
                ViewHolder5 viewHolder6 = new ViewHolder5(view6);
                return viewHolder6;

            // 场景电台  scene
            case 9:
                View view1_1 = LayoutInflater.from(mContext).inflate(R.layout.view_songbook1_recommend1_threeimg, parent, false);
                ViewHolder1 viewHolder1_1 = new ViewHolder1(view1_1, 9);
                return viewHolder1_1;


            // 今日推荐歌曲  recsong
            case 10:
                View view4 = LayoutInflater.from(mContext).inflate(R.layout.view_songbook1_recommend3_todayrecomend, parent, false);
                ViewHolder3 viewHolder4 = new ViewHolder3(view4, 10);
                return viewHolder4;


            // 原创音乐  mix_9
            case 11:
                View view2_3 = LayoutInflater.from(mContext).inflate(R.layout.view_songbook1_recommend2_listrecommend, parent, false);
                ViewHolder22 viewHolder2_3 = new ViewHolder22(view2_3, 11);
                return viewHolder2_3;

            // 最热MV  mix_5
            case 12:
                View view2_4 = LayoutInflater.from(mContext).inflate(R.layout.view_songbook1_recommend2_listrecommend, parent, false);
                ViewHolder2 viewHolder2_4 = new ViewHolder2(view2_4, 12);
                return viewHolder2_4;


            // 乐播节目  radio
            case 13:

                View view2_5 = LayoutInflater.from(mContext).inflate(R.layout.view_songbook1_recommend2_listrecommend, parent, false);
                ViewHolder2 viewHolder2_5 = new ViewHolder2(view2_5, 13);
                return viewHolder2_5;

            // 专栏  mod_7
            case 14:
                View view5 = LayoutInflater.from(mContext).inflate(R.layout.view_songbook1_recommend3_todayrecomend, parent, false);
                ViewHolder3 viewHolder5 = new ViewHolder3(view5, 14);
                return viewHolder5;


            // 广告
            case 15:
                break;


        }
        return new TestHolder(new TextView(mContext));
    }


    // 给布局赋值 添加点击事件
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();


        switch (getItemViewType(position)) {


            // 轮播图
            case 1:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                // 轮播图
                ArrayList<String> bitmaps = new ArrayList<>();
                for (int i = 0; i < bean.getResult().getFocus().getResult().size(); i++) {
                    bitmaps.add(bean.getResult().getFocus().getResult().get(i).getRandpic());
                }
                // 下面三句话顺序不能变
                // 还差圆点的点击事件
                viewHolder0.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);// 设置圆点
                viewHolder0.banner.setIndicatorGravity(BannerConfig.CENTER);// 圆点的位置
                viewHolder0.banner.setImages(bitmaps);
                break;


            // 音乐导航(三张图) entry  null
            case 2:

                //                imageLoader.get(bean.getResult().getEntry().getResult4().get(0).getIcon(), ImageLoader.getImageListener(viewHolder1.iv1_allsinger, R.mipmap.ic_launcher, R.mipmap.ic_launcher));
                // 利用第一个参数是网址,第二个参数是ImageView
                //                imageLoader.displayImage();
                //                VolleySingleton.getInstance().getImage(bean.getResult().getEntry().getResult4().get(0).getIcon(), viewHolder1.iv1_allsinger);
                // 毕加索的方法没有缓存
                //                Picasso.with(mContext).load(bean.getResult().getEntry().getResult4().get(0).getIcon()).into(viewHolder1.iv1_allsinger);


                final ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                viewHolder1.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder1.img);


                viewHolder1.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String url = bean.getResult().getEntry().getResult().get(i).getJump();
                        url = URLValues.SONGLIST_DETAIL_Front + url + URLValues.SONGLIST_DETAIL_BEHIND;
                        Log.d("SongBook1_Recommend_Rec", url);
                        // 接口回调
                        listener.onItemClick(viewHolder1.gv, url, i);
                    }
                });

                break;

            // 歌单推荐  diy 有..
            case 4:
                final ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder2.img);

                viewHolder2.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String url = bean.getResult().getDiy().getResult().get(i).getListid();
                        url = URLValues.SONGLIST_DETAIL_Front + url + URLValues.SONGLIST_DETAIL_BEHIND;
                        Log.d("SongBook1_Recommend_Rec", url);
                        // 接口回调

                        listener.onItemClick(viewHolder2.gv, url, 4);//

                    }
                });


                break;

            // 新碟上架  mix_1 null
            case 6:
                final ViewHolder2 viewHolder2_1 = (ViewHolder2) holder;
                viewHolder2_1.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder2_1.img);

                viewHolder2_1.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String url = bean.getResult().getMix_1().getResult().get(i).getType_id();


                        url = URLValues.SONGLIST_DETAIL_Front + url + URLValues.SONGLIST_DETAIL_BEHIND;

                        Log.d("SongBook1_Recommend_Rec", url);

                        // 接口回调
//                        listener.onItemClick(viewHolder2_1.gv, url, i);


                    }
                });


                break;

            // 热销专辑  mix_22 null
            case 7:
                final ViewHolder22 viewHolder2_2 = (ViewHolder22) holder;
                viewHolder2_2.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder2_2.img);

                viewHolder2_2.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String url = bean.getResult().getMix_22().getResult().get(i).getType_id();

                        url = URLValues.SONGLIST_DETAIL_Front + url + URLValues.SONGLIST_DETAIL_BEHIND;

                        // 接口回调
//                        listener.onItemClick(viewHolder2_2.gv, url, i);

                    }
                });


                break;

            // 商城固定入口(广告图片) mod_26
            case 8:
                ViewHolder5 viewHolder5 = (ViewHolder5) holder;
                imageLoader.displayImage(bean.getResult().getAd_small().getResult().get(0).getPic(), viewHolder5.img);

                viewHolder5.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String url = bean.getResult().getAd_small().getResult().get(0).getType_id();

                    }
                });


                break;


            // 场景电台  scene null
            case 9:
                final ViewHolder1 viewHolder1_1 = (ViewHolder1) holder;
                viewHolder1_1.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder1_1.img);

                viewHolder1_1.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String url = bean.getResult().getScene().getResult().getAction().get(i).getScene_id();

                        url = URLValues.SONGLIST_DETAIL_Front + url + URLValues.SONGLIST_DETAIL_BEHIND;

                        // 接口回调
//                        listener.onItemClick(viewHolder1_1.gv, url, i);
                    }
                });


                break;

            // 今日推荐歌曲  recsong null
            case 10:
                final ViewHolder3 viewHolder4 = (ViewHolder3) holder;
                viewHolder4.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder4.img);

                viewHolder4.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String url = bean.getResult().getRecsong().getResult().get(i).getSong_id();

                        url = URLValues.PLAY_FRONT + url + URLValues.PLAY_BEHIND;

                        // 接口回调
                        listener.onItemClick(viewHolder4.lv, url, 10);




//                        Intent intent = new Intent(MyApp.getContext(), MusicService.class);
//                        intent.putExtra("url", url);
//                        intent.putExtra("position", position);
//                        intent.putExtra("urlMore", arrayList1);
//                        getActivity().startService(intent);




                    }
                });


                break;

            // 原创音乐  mix_9 null
            case 11:
                final ViewHolder22 viewHolder2_3 = (ViewHolder22) holder;
                viewHolder2_3.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder2_3.img);

                viewHolder2_3.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String url = bean.getResult().getMix_9().getResult().get(i).getType_id();
                        url = URLValues.SONGLIST_DETAIL_Front + url + URLValues.SONGLIST_DETAIL_BEHIND;

                        // 接口回调
//                        listener.onItemClick(viewHolder2_3.gv, url, i);

                    }
                });


                break;

            // 最热MV  mix_5 null
            case 12:
                final ViewHolder2 viewHolder2_4 = (ViewHolder2) holder;
                viewHolder2_4.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder2_4.img);

                viewHolder2_4.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        String url = bean.getResult().getMix_5().getResult().get(i).getType_id();

                        url = URLValues.SONGLIST_DETAIL_Front + url + URLValues.SONGLIST_DETAIL_BEHIND;

                        // 接口回调
//                        listener.onItemClick(viewHolder2_4.gv, url, i);

                    }
                });


                break;

            // 乐播节目  radio
            case 13:
                final ViewHolder2 viewHolder2_5 = (ViewHolder2) holder;
                viewHolder2_5.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder2_5.img);

                viewHolder2_5.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                        String url = bean.getResult().getRadio().getResult().get(i).getAlbum_id();

                        url = URLValues.RECOMMAND_CAROUSE_SONG_FRONT + url + URLValues.RECOMMAND_CAROUSE_SONG_BEHIND;

//                        listener.onItemClick(viewHolder2_5.gv, url, i);


                    }
                });

                break;

            // 专栏  mod_7
            case 14:
                final ViewHolder3 viewHolder3_1 = (ViewHolder3) holder;
                viewHolder3_1.title.setText(bean.getModule().get(position).getTitle());
                imageLoader.displayImage(bean.getModule().get(position).getPicurl(), viewHolder3_1.img);

                viewHolder3_1.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        // 获得的就是网址
                        String url = bean.getResult().getMod_7().getResult().get(i).getType_id();
//                        listener.onItemClick(viewHolder3_1.lv, url, i);
                    }
                });

                break;

            //            default:
            //                // 给默认布局一个text
            //                TextView tv = (TextView) holder.itemView;
            //                tv.setText("hello");
        }
    }


    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : bean.getModule().size();// 有9个子list?
    }


    // 仅限表格布局的计算高度
    private ViewGroup.LayoutParams calculateHeight(ViewGroup listView, Adapter adapter) {

        // 计算高度
        int height = 0;
        for (int i = 0; i < adapter.getCount(); i += 3) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height;

        return params;
    }


    // listView

    private class ViewHolder0 extends RecyclerView.ViewHolder {
        private Banner banner;

        public ViewHolder0(View itemView) {
            super(itemView);
            banner = (Banner) itemView.findViewById(R.id.banner_songbook1_recommend);
        }
    }


    // 每日热点 + 场景电台
    private class ViewHolder1 extends RecyclerView.ViewHolder {

        private GridView gv;
        private ImageView img;
        private TextView title;
        private int pos;

        public ViewHolder1(View itemView, int pos) {
            super(itemView);

            this.pos = pos;
            gv = (GridView) itemView.findViewById(R.id.gv_songbook1_recommend1);
            img = (ImageView) itemView.findViewById(R.id.iv_songbook1_recommend1_three_img);
            title = (TextView) itemView.findViewById(R.id.tv_songbook1_recommend1_list_title);
            Adapter1 adapter = new Adapter1(MyApp.getContext());
            gv.setAdapter(adapter);
        }

        private class Adapter1 extends BaseAdapter {

            private Context context;

            public Adapter1(Context context) {
                this.context = context;
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Object getItem(int i) {
                return bean.getResult();
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                ViewHolder1_1 viewHolder1_1 = null;
                if (view == null) {
                    view = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.item_songbook1_recommend2_listrecommend, null);
                    viewHolder1_1 = new ViewHolder1_1(view);
                    view.setTag(viewHolder1_1);
                } else {
                    viewHolder1_1 = (ViewHolder1_1) view.getTag();
                }

                switch (pos) {
                    case 2:
                        viewHolder1_1.title.setText(bean.getResult().getEntry().getResult().get(i).getTitle());
                        imageLoader.displayImage(bean.getResult().getEntry().getResult().get(i).getIcon(), viewHolder1_1.img);
                        notifyDataSetChanged();
                        break;
                    case 6:
                        imageLoader.displayImage(bean.getResult().getEntry().getResult().get(i).getIcon(), viewHolder1_1.img);
                        notifyDataSetChanged();
                        break;
                }

                return view;
            }

            private class ViewHolder1_1 {

                private ImageView img;
                private TextView title;
                private TextView author;
                private TextView playedAmount;
                private TextView date;

                public ViewHolder1_1(View view) {

                    // View view = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.view_songbook1_recommend1_threeimg, null);
                    img = (ImageView) view.findViewById(R.id.iv_songbook1_recommend2_img);
                    title = (TextView) view.findViewById(R.id.tv_songbook1_recommend2_list_name);

                }
            }

        }


    }


    private class ViewHolder2 extends RecyclerView.ViewHolder {

        private GridView gv;
        private ImageView img;
        private TextView title;
        private int pos;


        public ViewHolder2(View itemView, final int pos) {
            super(itemView);

            this.pos = pos;
            gv = (GridView) itemView.findViewById(R.id.gv_songbook1_recommend2);
            img = (ImageView) itemView.findViewById(R.id.iv_songbook1_recommend2_listrecommend_img);
            title = (TextView) itemView.findViewById(R.id.tv_songbook1_recommend2_listrecommend_title);


            Adapter2 adapter2 = new Adapter2();


            //            // 计算高度
            //            int height = 0;
            //            for (int i = 0; i < adapter2.getCount(); i += 3) {
            //                View listItem = adapter2.getView(i, null, gv);
            //                listItem.measure(0, 0);
            //                height += listItem.getMeasuredHeight();
            //            }
            //            ViewGroup.LayoutParams params = gv.getLayoutParams();
            //            params.height = height;
            //            gv.setLayoutParams(params);


            ViewGroup.LayoutParams params = calculateHeight(gv, adapter2);
            gv.setLayoutParams(params);

            gv.setAdapter(adapter2);


        }


        private class Adapter2 extends BaseAdapter {


            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public Object getItem(int i) {
                return bean.getResult();
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {


                ViewHolder2_2 viewHodler2_2 = null;
                if (view == null) {

                    view = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.item_songbook1_recommend1_threeimg, null);
                    viewHodler2_2 = new ViewHolder2_2(view);
                    view.setTag(viewHodler2_2);
                } else {
                    viewHodler2_2 = (ViewHolder2_2) view.getTag();
                }

                switch (pos) {

                    case 4:
                        viewHodler2_2.song.setText(bean.getResult().getDiy().getResult().get(i).getTitle());
                        imageLoader.displayImage(bean.getResult().getDiy().getResult().get(i).getPic(), viewHodler2_2.img);
                        notifyDataSetChanged();
                        break;

                    case 6:
                        viewHodler2_2.song.setText(bean.getResult().getMix_1().getResult().get(i).getTitle());
                        viewHodler2_2.author.setText(bean.getResult().getMix_1().getResult().get(i).getAuthor());
                        imageLoader.displayImage(bean.getResult().getMix_1().getResult().get(i).getPic(), viewHodler2_2.img);
                        notifyDataSetChanged();
                        break;

                    case 12:
                        viewHodler2_2.song.setText(bean.getResult().getMix_5().getResult().get(i).getTitle());
                        viewHodler2_2.author.setText(bean.getResult().getMix_5().getResult().get(i).getAuthor());
                        imageLoader.displayImage(bean.getResult().getMix_5().getResult().get(i).getPic(), viewHodler2_2.img);
                        notifyDataSetChanged();
                        break;

                    case 13:
                        viewHodler2_2.song.setText(bean.getResult().getRadio().getResult().get(i).getTitle());
                        imageLoader.displayImage(bean.getResult().getRadio().getResult().get(i).getPic(), viewHodler2_2.img);
                        notifyDataSetChanged();
                        break;
                }


                return view;
            }

            private class ViewHolder2_2 {

                private ImageView img;
                private TextView song;
                private TextView author;

                public ViewHolder2_2(View view) {
                    img = (ImageView) view.findViewById(R.id.iv_songbook1_recommend_img);
                    song = (TextView) view.findViewById(R.id.tv_songbook1_recommend_name);
                    author = (TextView) view.findViewById(R.id.tv_songbook1_recommend_list_author);
                }
            }


        }
    }


    private class ViewHolder22 extends RecyclerView.ViewHolder {

        private GridView gv;
        private ImageView img;
        private TextView title;
        private int pos;


        public ViewHolder22(View itemView, int pos) {
            super(itemView);

            this.pos = pos;
            gv = (GridView) itemView.findViewById(R.id.gv_songbook1_recommend2);
            img = (ImageView) itemView.findViewById(R.id.iv_songbook1_recommend2_listrecommend_img);
            title = (TextView) itemView.findViewById(R.id.tv_songbook1_recommend2_listrecommend_title);


            Adapter2 adapter2 = new Adapter2();

            ViewGroup.LayoutParams params = calculateHeight(gv, adapter2);
            gv.setLayoutParams(params);


            gv.setAdapter(adapter2);


        }


        private class Adapter2 extends BaseAdapter {


            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Object getItem(int i) {
                return bean.getModule();
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                notifyDataSetChanged();
                ViewHolder2_2 viewHodler2_2 = null;
                if (view == null) {

                    view = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.item_songbook1_recommend1_threeimg, null);
                    viewHodler2_2 = new ViewHolder2_2(view);
                    view.setTag(viewHodler2_2);
                } else {
                    viewHodler2_2 = (ViewHolder2_2) view.getTag();
                }


                switch (pos) {

                    case 7:
                        viewHodler2_2.song.setText(bean.getResult().getMix_22().getResult().get(i).getTitle());
                        viewHodler2_2.author.setText(bean.getResult().getMix_22().getResult().get(i).getAuthor());
                        imageLoader.displayImage(bean.getResult().getMix_22().getResult().get(i).getPic(), viewHodler2_2.img);
                        break;

                    case 11:
                        viewHodler2_2.song.setText(bean.getResult().getMix_9().getResult().get(i).getTitle());
                        viewHodler2_2.author.setText(bean.getResult().getMix_9().getResult().get(i).getAuthor());
                        imageLoader.displayImage(bean.getResult().getMix_9().getResult().get(i).getPic(), viewHodler2_2.img);
                        break;

                }


                return view;
            }


            private class ViewHolder2_2 {

                private ImageView img;
                private TextView song;
                private TextView author;

                public ViewHolder2_2(View view) {
                    img = (ImageView) view.findViewById(R.id.iv_songbook1_recommend_img);
                    song = (TextView) view.findViewById(R.id.tv_songbook1_recommend_name);
                    author = (TextView) view.findViewById(R.id.tv_songbook1_recommend_list_author);
                }
            }


        }
    }


    private class ViewHolder3 extends RecyclerView.ViewHolder {

        private ListView lv;
        private ImageView img;
        private TextView title;
        private int pos;


        public ViewHolder3(View itemView, int pos) {
            super(itemView);

            this.pos = pos;
            lv = (ListView) itemView.findViewById(R.id.lv_songbook1_recommend3_todayrecommend);
            img = (ImageView) itemView.findViewById(R.id.iv_songbook1_recommend3_img);
            title = (TextView) itemView.findViewById(R.id.tv_songbook1_recommend3_title);


            Adapter3 adapter3 = new Adapter3();

            // 计算高度(for里面应该 +1!!!!)
            int height = 0;
            for (int i = 0; i < adapter3.getCount(); i++) {
                View listItem = adapter3.getView(i, null, lv);
                listItem.measure(0, 0);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = lv.getLayoutParams();
            params.height = height + 6;// 添加一点高度吧...
            lv.setLayoutParams(params);
            lv.setAdapter(adapter3);

        }

        private class Adapter3 extends BaseAdapter {


            @Override
            public int getCount() {
                // 传入的参数进行判断
                switch (pos) {
                    case 10:
                        return 3;
                    case 14:
                        return 4;
                }
                return 0;
            }


            @Override
            public Object getItem(int i) {
                return bean.getResult();
            }

            @Override
            public long getItemId(int i) {
                return i;
            }


            // itemCount数目
            // lv和按钮的点击事件
            // 2个viewHolder
            // 几个参数
            // lv的长度问题(设置固定高度吧)


            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {


                switch (pos) {
                    case 10:

                        ViewHolder3_4 viewHodler3_4 = null;
                        if (view == null) {
                            view = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.item_songbook1_recommend4_recommendsong, null);
                            viewHodler3_4 = new ViewHolder3_4(view);
                            view.setTag(viewHodler3_4);
                        } else {
                            viewHodler3_4 = (ViewHolder3_4) view.getTag();
                        }
                        viewHodler3_4.song.setText(bean.getResult().getRecsong().getResult().get(i).getTitle());
                        viewHodler3_4.author.setText(bean.getResult().getRecsong().getResult().get(i).getAuthor());
                        imageLoader.displayImage(bean.getResult().getRecsong().getResult().get(i).getPic_premium(), viewHodler3_4.img);

                        return view;

                    case 14:
                        ViewHolder3_3 viewHodler3_3 = null;
                        if (view == null) {
                            view = LayoutInflater.from(MyApp.getContext()).inflate(R.layout.item_songbook1_recommend5_column, null);
                            viewHodler3_3 = new ViewHolder3_3(view);
                            view.setTag(viewHodler3_3);
                        } else {
                            viewHodler3_3 = (ViewHolder3_3) view.getTag();
                        }
                        viewHodler3_3.song.setText(bean.getResult().getMod_7().getResult().get(i).getTitle());
                        viewHodler3_3.author.setText(bean.getResult().getMod_7().getResult().get(i).getDesc());
                        imageLoader.displayImage(bean.getResult().getMod_7().getResult().get(i).getPic(), viewHodler3_3.img);

                        return view;

                    default:
                        Log.d("Adapter3", "default");
                        break;
                }


                return null;
            }


            private class ViewHolder3_3 {

                private ImageView img;
                private TextView song;
                private TextView author;

                public ViewHolder3_3(View view) {
                    img = (ImageView) view.findViewById(R.id.iv_songbook1_recommend5_pic1);
                    song = (TextView) view.findViewById(R.id.tv_songbook1_recommend5_song1);
                    author = (TextView) view.findViewById(R.id.tv_songbook1_recommend5_author1);
                }
            }

            private class ViewHolder3_4 {

                private ImageView img;
                private TextView song;
                private TextView author;

                public ViewHolder3_4(View view) {
                    img = (ImageView) view.findViewById(R.id.iv_songbook1_recommend4_pic1);
                    song = (TextView) view.findViewById(R.id.tv_songbook1_recommend4_song1);
                    author = (TextView) view.findViewById(R.id.tv_songbook1_recommend4_author1);
                }
            }


        }


    }


    private class ViewHolder5 extends RecyclerView.ViewHolder {

        private ImageView img;

        public ViewHolder5(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.iv_songbook1_recommend6_advertisement);
        }
    }


    // 一个默认的default
    private class TestHolder extends RecyclerView.ViewHolder {
        public TestHolder(TextView textView) {
            super(textView);
        }
    }


}
