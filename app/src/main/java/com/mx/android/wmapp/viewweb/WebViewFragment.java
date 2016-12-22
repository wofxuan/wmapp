package com.mx.android.wmapp.viewweb;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.mx.wmapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {
    public String openUrl;
    public String curURLTitle;
    public WebView webView;
    private View rootView;
    private WebViewClient homeWebViewClient;
    private WebChromeClient homeWebChromeClient;

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
            webView = (WebView) rootView.findViewById(R.id.webviewFragment);
            initHome();
        }
        return rootView;
    }

    private void initHome() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);

        homeWebViewClient = new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ((IWebViewEvent) getActivity()).onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ((IWebViewEvent) getActivity()).onPageFinished(view, url);
            }
        };

        homeWebChromeClient = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                ((IWebViewEvent) getActivity()).onProgressChanged(view, newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                curURLTitle = title;
            }
        };


        webView.setWebChromeClient(homeWebChromeClient);
        webView.setWebViewClient(homeWebViewClient);

        if (getArguments() != null) {
            openUrl = getArguments().getString("openUrl");

            webView.loadUrl(openUrl);
        }
    }

}
