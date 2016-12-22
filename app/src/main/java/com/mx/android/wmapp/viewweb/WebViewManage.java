package com.mx.android.wmapp.viewweb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-12-21.
 */
public class WebViewManage {
    public List<WebViewFragment> listFragment;
    private Context mContext;
    private FragmentManager fm;
    private FrameLayout mFrame;
    private WebViewFragment currFragment;

    public WebViewManage(Context context, FrameLayout fragment) {
        mContext = context;
        mFrame = fragment;
        fm = ((Activity) context).getFragmentManager();
        listFragment = new ArrayList();
    }

    private void showPage(WebViewFragment fragment){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(mFrame.getId(), fragment);
        currFragment = fragment;
        transaction.commit();
    }

    public void addNewWebView(String URL) {
        WebViewFragment webFragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("openUrl", URL);
        webFragment.setArguments(args);

        ((IWebViewEvent)mContext).onNewPage();

        showPage(webFragment);
        listFragment.add(webFragment);
    }

    public void delWebView(int index) {
        if (listFragment.size() >= index) {
            listFragment.remove(index);
        }
    }

    public void goBack() {
        currFragment.webView.goBack();
    }

    public void goForward() {
        currFragment.webView.goForward();
    }

    public void reload() {
        currFragment.webView.reload();
    }

    public String currUrl() {
        return currFragment.webView.getUrl();
    }

    public boolean canGoBack() {
        return currFragment.webView.canGoBack();
    }

    public boolean canGoForward() {
        return currFragment.webView.canGoForward();
    }

    public String currUrlTitle() {
        return currFragment.curURLTitle;
    }

    public void newOrSelWindow() {
        final String[] items = new String[listFragment.size()];
        for (int i = 0; i < listFragment.size(); i++) {
            WebViewFragment webFragment = listFragment.get(i);
            items[i] = webFragment.curURLTitle;
        }
        AlertDialog.Builder listDialog = new AlertDialog.Builder(mContext);
        listDialog.setTitle("Fragment列表");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebViewFragment webFragment = listFragment.get(which);
                showPage(webFragment);
            }
        });
        listDialog.show();
    }
}
