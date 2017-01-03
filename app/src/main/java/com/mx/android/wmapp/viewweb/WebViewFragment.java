package com.mx.android.wmapp.viewweb;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.mx.wmapp.R;

import java.util.ArrayList;

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


        // 长按点击事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                String[] items = {"新窗口打开", "后台窗口打开", "复制链接"};
                ArrayList<String> ao = new ArrayList();
                ao.add("新窗口打开");
                ao.add("后台窗口打开");
                ao.add("复制链接");
                final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {

                    if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                            hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        ao.add("保存图片");
                    }
                    String[] items = ao.toArray(new String[]{});
                    ;

                    AlertDialog.Builder listDialog = new AlertDialog.Builder(getActivity());
                    listDialog.setTitle("");
                    listDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url = hitTestResult.getExtra();
                            switch (which) {
                                case 0:
                                    ((IWebViewEvent) getActivity()).onAddNewPage(url, false);
                                    break;
                                case 1:
                                    ((IWebViewEvent) getActivity()).onAddNewPage(url, true);
                                    break;
                                case 2:
                                    // 从API11开始android推荐使用android.content.ClipboardManager
                                    // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                                    // 将文本内容放到系统剪贴板里。
                                    cm.setText(url);
                                    Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_LONG).show();
                                    break;
                                case 3:
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    listDialog.show();
                }
                return true;
            }
        });

        if (getArguments() != null) {
            openUrl = getArguments().getString("openUrl");

            webView.loadUrl(openUrl);
        }
    }

}
