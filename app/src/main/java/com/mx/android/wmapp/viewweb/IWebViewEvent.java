package com.mx.android.wmapp.viewweb;

import android.graphics.Bitmap;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016-12-21.
 */
public interface IWebViewEvent {
    public void onPageFinished(WebView view, String url);
    public void onPageStarted(WebView view, String url, Bitmap favicon);
    public void onProgressChanged(WebView view, int newProgress);
    public void onNewPageShow();
    public void onAddNewPage(String URL, boolean isHide);
    public void onDelPage(Integer indexWeb);
    public void onShowPage(Integer indexWeb);
}
