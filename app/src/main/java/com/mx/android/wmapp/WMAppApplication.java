package com.mx.android.wmapp;

import android.app.Application;

/**
 * Created by Administrator on 2016-10-31.
 */
public class WMAppApplication extends Application {
    private String openDirHis;//记录上传打开的目录

    public String getOpenDirHis() {
        return openDirHis;
    }

    public void setOpenDirHis(String openDirHis) {
        this.openDirHis = openDirHis;
    }
}
