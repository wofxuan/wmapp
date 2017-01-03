package com.mx.android.wmapp.viewweb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.android.mx.wmapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-21.
 */
public class WebViewManage {
    public List<WebViewFragment> listFragment;
    private Context mContext;
    private FragmentManager fm;
    private FrameLayout mFrame;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private WebListAdspter listAdapter;
    private WebViewFragment currFragment;

    public WebViewManage(Context context, FrameLayout fragment) {
        mContext = context;
        mFrame = fragment;
        fm = ((Activity) context).getFragmentManager();
        listFragment = new ArrayList();
        mBuilder = new AlertDialog.Builder(mContext);
    }

    private void showPage(WebViewFragment fragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        if (fragment.isHidden()) {
            transaction.show(fragment);
        } else {
            transaction.replace(mFrame.getId(), fragment);
        }

        currFragment = fragment;
        transaction.commit();
    }

    public void addNewWebView(String URL) {
        WebViewFragment webFragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("openUrl", URL);
        webFragment.setArguments(args);

        ((IWebViewEvent) mContext).onNewPageShow();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(mFrame.getId(), webFragment);
        transaction.show(webFragment);
        transaction.commit();

        currFragment = webFragment;
        listFragment.add(webFragment);
    }

    public void addHideNewWebView(String URL) {
        WebViewFragment webFragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("openUrl", URL);
        webFragment.setArguments(args);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(mFrame.getId(), webFragment);
        transaction.hide(webFragment);
        transaction.commit();

        ((IWebViewEvent) mContext).onNewPageShow();
        listFragment.add(webFragment);
    }

    public void delWebView(int index) {
        if (listFragment.size() >= index) {
            WebViewFragment webFragment = listFragment.get(index);
            if (webFragment == currFragment) {
                listFragment.remove(index);
                if (listFragment.size() > 0) {
                    showWebView(listFragment.size() - 1);
                }
            } else {
                listFragment.remove(index);
            }

            mDialog.dismiss();
        }
    }

    public void showWebView(int index) {
        if (listFragment.size() > index) {
            WebViewFragment webFragment = listFragment.get(index);
            showPage(webFragment);
            mDialog.dismiss();
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
        //inflate 你需要的layout
        View contents = View.inflate(mContext, R.layout.web_diag_listview, null);

        ListView listView = (ListView) contents.findViewById(R.id.web_item_list);

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < listFragment.size(); i++) {
            WebViewFragment webFragment = listFragment.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", webFragment.curURLTitle);
            list.add(map);
        }

        listAdapter = new WebListAdspter(mContext, list);
        listView.setAdapter(listAdapter);

        mBuilder.setTitle("打开列表")
                .setCancelable(true)
                .setView(contents); //关键是这里，将alertdialog和layout联系在一起

        mDialog = mBuilder.show();
    }
}
