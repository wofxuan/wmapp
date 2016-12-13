package com.mx.android.wmapp.entity;

import com.android.mx.wmapp.R;

/**
 * Created by Administrator on 2016-06-07.
 */
public class OptData {
    public static final int intall_Info = 1;
    public static final int sys_Info = 2;
    public static final int cur_Pos = 3;
    public static final int video_player = 4;
    public static final int view_img = 5;
    public static final int view_web = 6;

    public static final OptData[] OptDataS = {
            new OptData(intall_Info, "001", "安装信息", R.mipmap.install),
            new OptData(sys_Info, "002", "系统状态", R.mipmap.system),
            new OptData(cur_Pos, "003", "当前位置", R.mipmap.location),
            new OptData(video_player, "004", "播放视频", R.mipmap.videoplayer),
            new OptData(view_img, "005", "查看图片", R.mipmap.viewimg),
            new OptData(view_web, "006", "网页浏览", R.mipmap.viewweb),
    };
    public long sid;
    public String mid;
    public String title;
    public int imgId;

    public OptData(long sid, String mid, String title, int imgId) {
        this.sid = sid;
        this.mid = mid;
        this.title = title;
        this.imgId = imgId;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
