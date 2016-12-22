package com.mx.android.wmapp.viewweb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mx.wmapp.R;

public class ViewWebActivity extends Activity implements View.OnClickListener, IWebViewEvent {

    RelativeLayout main_layout;
    LinearLayout index_view;
    EditText index_title_edit;
    WebViewManage webViewManage;
    FrameLayout index_ShowWebView;
    LinearLayout index_bottom_menu_goback;
    LinearLayout index_bottom_menu_nogoback;
    LinearLayout index_bottom_menu_goforward;
    LinearLayout index_bottom_menu_nogoforward;
    LinearLayout index_bottom_menu_gohome;
    LinearLayout index_bottom_menu_nogohome;
    LinearLayout index_bottom_menu_new_window;
    FrameLayout index_bottom_menu_more;
    ImageView index_title_refresh;
    ProgressBar index_title_progress;
    LinearLayout bottom_dialog;
    View index_background;
    RelativeLayout popup_exit;
    RelativeLayout popup_open_bookmark;
    RelativeLayout popup_add_bookmark;
    LinearLayout search_view;
    EditText search_title_edit;
    Button search_title_cancel;
    ImageView search_title_url_clear;
    Button search_title_go;
    TextView index_bottom_tab_count;
    //    WebViewClient homeWebViewClient;
//    WebChromeClient homeWebChromeClient;
    TextWatcher search_title_edit_changed = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //是否显示清除
            if (search_title_edit.getText().toString().length() > 0) {
                search_title_url_clear.setVisibility(View.VISIBLE);
                search_title_go.setVisibility(View.VISIBLE);
                search_title_cancel.setVisibility(View.GONE);
            } else {
                search_title_url_clear.setVisibility(View.GONE);
                search_title_go.setVisibility(View.GONE);
                search_title_cancel.setVisibility(View.VISIBLE);
            }
            //是否显示前往
            //是否显示取消
        }
    };
    //主页地址
    private String home_url = "http://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_web);

        index_ShowWebView = (FrameLayout) findViewById(R.id.index_WebViewFragment);

        index_view = (LinearLayout) findViewById(R.id.index_view);
        index_title_edit = (EditText) index_view.findViewById(R.id.index_title_edit);

        index_bottom_menu_goback = (LinearLayout) findViewById(R.id.index_bottom_menu_goback);
        index_bottom_menu_nogoback = (LinearLayout) findViewById(R.id.index_bottom_menu_nogoback);
        index_bottom_menu_goforward = (LinearLayout) findViewById(R.id.index_bottom_menu_goforward);
        index_bottom_menu_nogoforward = (LinearLayout) findViewById(R.id.index_bottom_menu_nogoforward);
        index_bottom_menu_gohome = (LinearLayout) findViewById(R.id.index_bottom_menu_gohome);
        index_bottom_menu_nogohome = (LinearLayout) findViewById(R.id.index_bottom_menu_nogohome);
        index_bottom_menu_new_window = (LinearLayout) findViewById(R.id.index_bottom_menu_new_window);
        index_bottom_menu_more = (FrameLayout) findViewById(R.id.index_bottom_menu_more);
        index_bottom_tab_count = (TextView) findViewById(R.id.index_bottom_tab_count);

        index_title_refresh = (ImageView) index_view.findViewById(R.id.index_title_refresh);
        index_title_progress = (ProgressBar) index_view.findViewById(R.id.index_title_progress);
        index_background = (View) findViewById(R.id.index_background);
        bottom_dialog = (LinearLayout) findViewById(R.id.bottom_dialog);

        popup_open_bookmark = (RelativeLayout) findViewById(R.id.popup_open_bookmark);
        popup_add_bookmark = (RelativeLayout) findViewById(R.id.popup_add_bookmark);
        popup_exit = (RelativeLayout) findViewById(R.id.popup_exit);

        index_bottom_menu_more.setOnClickListener(this);
        index_bottom_menu_gohome.setOnClickListener(this);
        index_bottom_menu_goback.setOnClickListener(this);
        index_bottom_menu_goforward.setOnClickListener(this);
        index_bottom_menu_new_window.setOnClickListener(this);

        index_title_edit.setOnClickListener(this);
        index_title_refresh.setOnClickListener(this);
        index_background.setOnClickListener(this);

        popup_open_bookmark.setOnClickListener(this);
        popup_add_bookmark.setOnClickListener(this);
        popup_exit.setOnClickListener(this);

        search_view = (LinearLayout) findViewById(R.id.search_view);
        search_title_edit = (EditText) search_view.findViewById(R.id.search_title_edit);
        search_title_cancel = (Button) search_view.findViewById(R.id.search_title_cancel);
        search_title_go = (Button) search_view.findViewById(R.id.search_title_go);
        search_title_url_clear = (ImageView) search_view.findViewById(R.id.search_title_url_clear);

        search_title_edit.addTextChangedListener(search_title_edit_changed);
        search_title_cancel.setOnClickListener(this);
        search_title_url_clear.setOnClickListener(this);
        search_title_go.setOnClickListener(this);

        webViewManage = new WebViewManage(this, index_ShowWebView);
        webViewManage.addNewWebView(home_url);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) search_title_edit.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.search_title_cancel:
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                index_view.setVisibility(View.VISIBLE);
                search_view.setVisibility(View.GONE);
                search_title_edit.clearFocus();
                break;
            case R.id.search_title_go:
                String goUrl = search_title_edit.getText().toString();
                if (goUrl.indexOf("http://") < 0) {
                    goUrl = "http://" + goUrl;
                    search_title_edit.setText(goUrl);
                }
                search_title_cancel.callOnClick();
                webViewManage.addNewWebView(goUrl);
                break;
            case R.id.search_title_url_clear:
                search_title_edit.setText("");
                break;
            case R.id.index_bottom_menu_goback:
                webViewManage.goBack();
                break;
            case R.id.index_bottom_menu_goforward:
                webViewManage.goForward();
                break;
            case R.id.index_bottom_menu_gohome:
                webViewManage.addNewWebView(home_url);
                break;
            case R.id.index_bottom_menu_more:
                if (bottom_dialog.getVisibility() == View.GONE) {
                    doMenuMore(true);
                } else {
                    doMenuMore(false);
                }
                break;
            case R.id.index_bottom_menu_new_window:
                newWindow();
                break;
            case R.id.index_title_edit:
                index_view.setVisibility(View.GONE);
                search_view.setVisibility(View.VISIBLE);
                search_title_edit.requestFocus();
                inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.index_title_refresh:
                webViewManage.reload();
                break;
            case R.id.index_background:
                bottom_dialog.setVisibility(View.GONE);
                index_background.setVisibility(View.GONE);
                break;
            case R.id.popup_open_bookmark:
                startActivityForResult(new Intent(this, FavAndHisActivity.class), 0);
                doMenuMore(false);
                break;
            case R.id.popup_add_bookmark:
                FavoritesManager favoritesManager = new FavoritesManager(this);
                favoritesManager.addFavorite(webViewManage.currUrlTitle(), webViewManage.currUrl());
                Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
                doMenuMore(false);
                break;
            case R.id.popup_exit:
                finish();
//                onDestroy();
//                System.exit(0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0:
                if (data != null) {
                    webViewManage.addNewWebView(data.getStringExtra("url"));
                }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webViewManage.canGoBack()) {
            webViewManage.goBack();//goBack()表示返回WebView的上一页面
            return true;
        }
        finish();//结束退出程序
        return false;
    }

    private void doMenuMore(boolean visiable) {
        //动画设置有问题
        if (visiable) {
            if (bottom_dialog.getVisibility() != View.GONE) return;
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.menu_show);
            index_background.setVisibility(View.VISIBLE);
            bottom_dialog.startAnimation(animation);
            bottom_dialog.setVisibility(View.VISIBLE);
//                    bottom_dialog.setAnimation();
        } else {
            if (bottom_dialog.getVisibility() == View.GONE) return;
            bottom_dialog.setAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_hide));
            index_background.setVisibility(View.GONE);
            bottom_dialog.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.equals(home_url + "/")) {
            index_bottom_menu_gohome.setVisibility(View.GONE);
            index_bottom_menu_nogohome.setVisibility(View.VISIBLE);
        } else {
            index_bottom_menu_gohome.setVisibility(View.VISIBLE);
            index_bottom_menu_nogohome.setVisibility(View.GONE);
        }
        if (webViewManage.canGoForward()) {
            index_bottom_menu_goforward.setVisibility(View.VISIBLE);
            index_bottom_menu_nogoforward.setVisibility(View.GONE);
        } else {
            index_bottom_menu_goforward.setVisibility(View.GONE);
            index_bottom_menu_nogoforward.setVisibility(View.VISIBLE);
        }
        if (webViewManage.canGoBack()) {
            index_bottom_menu_goback.setVisibility(View.VISIBLE);
            index_bottom_menu_nogoback.setVisibility(View.GONE);
        } else {
            index_bottom_menu_goback.setVisibility(View.GONE);
            index_bottom_menu_nogoback.setVisibility(View.VISIBLE);
        }
        Integer oldcount = Integer.valueOf(index_bottom_tab_count.getText().toString());
        oldcount++;
        index_bottom_tab_count.setText(oldcount.toString());
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        index_title_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            index_title_progress.setVisibility(View.GONE);
        } else {
            index_title_progress.setProgress(newProgress);
        }
    }

    private void newWindow() {
        webViewManage.newOrSelWindow();
    }
}
