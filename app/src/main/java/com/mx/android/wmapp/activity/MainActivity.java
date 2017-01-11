package com.mx.android.wmapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.mx.wmapp.R;
import com.mx.android.wmapp.ImgView.ImgViewActivity;
import com.mx.android.wmapp.adapter.OptAdapter;
import com.mx.android.wmapp.adapter.RecyclerViewItemClick;
import com.mx.android.wmapp.base.BaseActivity;
import com.mx.android.wmapp.common.CommFun;
import com.mx.android.wmapp.entity.OptData;
import com.mx.android.wmapp.viewweb.ViewWebActivity;

public class MainActivity extends BaseActivity {
    private RecyclerView lstOpt = null;
    private OptAdapter lstItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstOpt = (RecyclerView) findViewById(R.id.lstOpt);
//        lstOpt.setLayoutManager(new LinearLayoutManager(this));
        lstOpt.setLayoutManager(new GridLayoutManager(this, 3));
        lstItem = new OptAdapter(this);
        lstOpt.setAdapter(lstItem);
        lstItem.setOnRecyclerViewItemClickListener(new OnItemClickListenerImpl()); // 单击选项
    }

    @Override //重写onCreateOptionMenu(Menu menu)方法，当菜单第一次被加载时调用
    public boolean onCreateOptionsMenu(Menu menu) {
        //填充选项菜单（读取XML文件、解析、加载到Menu组件上）
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override //重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                System.exit(0);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class OnItemClickListenerImpl implements RecyclerViewItemClick {
        @Override
        public void onItemClick(View view, int position) {
            OptData data = lstItem.getItem(position);
            long sid = data.sid;
            if (sid == OptData.intall_Info) {
                CommFun.showPackageList(MainActivity.this);
            } else if (sid == OptData.sys_Info) {
                startActivity(new Intent(MainActivity.this, SysStateActivity.class));
            } else if (sid == OptData.cur_Pos) {
                startActivity(new Intent(MainActivity.this, GetLocationActivity.class));
            } else if (sid == OptData.video_player){
                startActivity(new Intent(MainActivity.this, VideoPlayerActivity.class));
            } else if (sid == OptData.view_img){
                startActivity(new Intent(MainActivity.this, ImgViewActivity.class));
            }else if (sid == OptData.view_web){
                startActivity(new Intent(MainActivity.this, ViewWebActivity.class));
            }
        }

        @Override
        public void onItemLongClick(View view, int position) {
            OptData data = lstItem.getItem(position);
            String name = data.title;
            Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
        }
    }
}
