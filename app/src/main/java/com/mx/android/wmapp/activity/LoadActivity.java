package com.mx.android.wmapp.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.android.mx.wmapp.R;
import com.mx.android.wmapp.base.BaseActivity;
import com.mx.android.wmapp.entity.EventCenter;

public class LoadActivity extends BaseActivity {

    private static final int LOAD_DISPLAY_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                                 /* Create an Intent that will start the Main WordPress Activity. */
                Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
                LoadActivity.this.startActivity(mainIntent);
                LoadActivity.this.finish();
            }
        }, LOAD_DISPLAY_TIME); //1500 for release
    }

    @Override
    protected boolean isApplyButterKnife() {
        return true;
    }

    @Override
    protected boolean isApplyEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_load;
    }
}
