package com.mx.android.wmapp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mx.android.wmapp.entity.EventCenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        if (isApplyButterKnife()) binder = ButterKnife.bind(this);//必须放到setContentView之后
        if (isApplyEventBus()) EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        if (isApplyEventBus()) EventBus.getDefault().unregister(this);
        if (isApplyButterKnife()) binder.unbind();
        super.onDestroy();
    }

    @Subscribe
    public void onMessageEvent(EventCenter event) {
        if (event != null) {
            onEventComing(event);
        }
    }

    protected abstract int getContentView();

    protected abstract boolean isApplyButterKnife();

    protected abstract boolean isApplyEventBus();

    protected abstract void onEventComing(EventCenter eventCenter);
}
