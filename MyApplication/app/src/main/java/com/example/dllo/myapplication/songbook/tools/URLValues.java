package com.example.dllo.myapplication.songbook.tools;

/**
 * Created by dllo on 16/5/23.
 */
public class URLValues {


    // 第一页G
    public static final String RECOMMAND_MAIN = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=0&method=baidu.ting.plaza.index&cuid=A1154C8AA9D92571F575C61727020BE6&bduss=RaVHN4U3lKNHJzTndGbFRicHNucU1vQmcySjB3ZFEwTWZTSH5EcTlmfjVNUWRZQVFBQUFBJCQAAAAAAAAAAAEAAAD3VhVGvrLRqcj0xq7T6gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPmk31f5pN9XRD%20HTTP/1.1";


    // 首页轮播图G
    public static final String RECOMMAND_CAROUSE = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.plaza.getFocusPic&format=json&from=ios&version=5.2.3&from=ios&channel=appstore";

    // 首页热门歌单
    public static final String RECOMMAND_HOT = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.getHotGeDanAndOfficial&num=4&version=5.2.3&from=ios&channel=appstore";

    // 首页热门歌单
    public static final String ALL_SONGLIST = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedan&page_no=1&page_size=30&from=ios&version=5.2.3&from=ios&channel=appstore";


    // 音乐 - 榜单G
    public static final String MUSICSTORE_TOP = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billCategory&format=json&from=ios&version=5.2.1&from=ios&channel=appstore";



    // 音乐 - 推荐 - 歌曲分类 (里面压根什么都没有)
    public static final String MUSICTYPE = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=0&method=baidu.ting.tag.getAllTag&format=json&from=android&version=5.9.0.0%20HTTP/1.1%20cuid:%20A1154C8AA9D92571F575C61727020BE6";




    // 音乐 - 歌单 - 最热/最新G
    public static final String NEWESTMUSICLIST = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=1&method=baidu.ting.ugcdiy.getChanneldiy&param=W%2BiDuPq2yxc8wGDCVz7TPOiKkuHyOjYEPlgwbQ3%2FZ4VSxCySIEXMorEKwts9lPNpqqk2VsJtlN664uU27R4vz2NQ%2BYy17fKd1wMJ%2FDrQVpAWeEvzDktR%2FJdLlW%2BsGxna&timestamp=1474614518&sign=ff2cc05fc2063921014aada124a26c8b";
    public static final String HOTESTMUSICLIST = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=1&method=baidu.ting.ugcdiy.getChanneldiy&param=WRZ7j5ADhYE2v%2FhAlCEMZJuRtGFeSHxB840ojko%2BVB3M958LQOsDjNPKT8JoHm9ckU%2BVzNAwTxM71nmU81LatTzNKU7xNSo95v3Whi%2Fx13yos1LbiWUzKHCue3iuop7J&timestamp=1474614530&sign=83866625313e32a12e8c9bbb7677cff5";


    // 视频 - 最热/最新G
    public static final String NEWESTVEDIO  = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.0.0&channel=1413c&operator=1&provider=11%2C12&method=baidu.ting.mv.searchMV&format=json&order=0&page_num=1&page_size=20&query=%E5%85%A8%E9%83%A8";
    public static final String HOTTESTVEDIO = "";


    // 拼接的网址




    // 歌单列表—轮播图点击后传入参数 (注：轮播图上数据不定含有某些数据与之不同，需要判断type给定不同的url接入参数来解析)

    // 歌单推荐的拼接url 第二列(歌单)依然可用
    public static final String SONGLIST_DETAIL_Front = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.diy.gedanInfo&from=ios&listid=";
    public static final String SONGLIST_DETAIL_BEHIND = "&format=json&offset=0&size=50&from=ios&fields=title,song_id,author,resource_type,havehigh,is_new,has_mv_mobile,album_title,ting_uid,album_id,charge,all_rate&version=5.2.1&from=ios&channel=appstore";



    // 第三列 - 排行榜G
    public static final String TOP_SONG_FRONT = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=";
    public static final String TOP_SONG_BEHIND = "&format=json&offset=0&size=50&from=ios&fields=title,song_id,author,resource_type,havehigh,is_new,has_mv_mobile,album_title,ting_uid,album_id,charge,all_rate&version=5.2.1&from=ios&channel=appstore";



    // 乐播节目? 并不是G
    public static final String RECOMMAND_CAROUSE_SONG_FRONT = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.album.getAlbumInfo&album_id=";
    public static final String RECOMMAND_CAROUSE_SONG_BEHIND = "&format=json&from=ios&version=5.2.5&from=ios&channel=appstore";



    // 单独一首歌曲的Url 内容无法解析
    public static final String PLAY_FRONT = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music&method=baidu.ting.song.play&format=json&callback=&songid=";
    public static final String PLAY_BEHIND = "&_=1413017198449";




}
