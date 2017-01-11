package com.mx.android.wmapp.ImgView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mx.mxlib.DFSelectActivity;
import com.android.mx.wmapp.R;
import com.mx.android.wmapp.base.BaseActivity;
import com.mx.android.wmapp.customview.ZoomImageView;

public class ImgViewActivity extends BaseActivity implements View.OnClickListener {
    private static final int OPEN_FILE_REQUEST_CODE = 1;
    private static final int OPEN_DIR_REQUEST_CODE = 2;
    Context context;
    private ZoomImageView zoomImg;
    private Button btnbef;
    private Button btnnext;
    private Button btnpageNo;
    private TextView tooolBarCaption;
    private ImgViewManage mImgViewManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_view);
        context = this;

        tooolBarCaption = (TextView) findViewById(R.id.toolBarCaption);
        Toolbar toolbar = (Toolbar) findViewById(R.id.imgtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.imgview_file_open:
                        intent = new Intent(context, DFSelectActivity.class);
                        intent.putExtra("type", DFSelectActivity.TypeOpen);
                        intent.putExtra("result_code", OPEN_FILE_REQUEST_CODE);
                        intent.putExtra("defaultDir", DFSelectActivity.getOpenDirHis());
                        intent.putExtra("fileType", new String[]{"*.*"});
                        startActivityForResult(intent, OPEN_FILE_REQUEST_CODE);
                        break;
                    case R.id.imgview_dir_open:
                        intent = new Intent(context, DFSelectActivity.class);
                        intent.putExtra("type", DFSelectActivity.TypeOpen);
                        intent.putExtra("result_code", OPEN_DIR_REQUEST_CODE);
                        intent.putExtra("defaultDir", DFSelectActivity.getOpenDirHis());
                        startActivityForResult(intent, OPEN_DIR_REQUEST_CODE);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        btnbef = (Button) findViewById(R.id.btn_bef);
        btnbef.setOnClickListener(this);

        btnnext = (Button) findViewById(R.id.btn_next);
        btnnext.setOnClickListener(this);

        btnpageNo = (Button) findViewById(R.id.btn_pageNo);
        btnpageNo.setOnClickListener(this);

        zoomImg = (ZoomImageView) findViewById(R.id.image);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.viewimg);
        zoomImg.setImage(bitmap);

        mImgViewManage = new ImgViewManage(this, zoomImg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.imgview_menu, menu);
        return true;
    }

    //获取路径里面的文件名
    public String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.length();
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String aCurrPath = data.getStringExtra("selectPath");
        if (requestCode == OPEN_FILE_REQUEST_CODE) {
            if (resultCode == OPEN_FILE_REQUEST_CODE) {
                Toast.makeText(context, aCurrPath, Toast.LENGTH_SHORT).show();
                mImgViewManage.IniImgList(aCurrPath, 0);
                tooolBarCaption.setText(getFileName(aCurrPath));
            }
        } else if (requestCode == OPEN_DIR_REQUEST_CODE) {
            if (resultCode == OPEN_DIR_REQUEST_CODE) {
                mImgViewManage.IniImgList(aCurrPath, 1);
                DFSelectActivity.setOpenDirHis(aCurrPath);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == btnbef.getId()) {
            mImgViewManage.BeforePic();
            btnpageNo.setText("页码(" + mImgViewManage.mCurrPos + ")");
        } else if (v.getId() == btnnext.getId()) {
            mImgViewManage.NextPic();
            btnpageNo.setText("页码(" + mImgViewManage.mCurrPos + ")");
        } else if (v.getId() == btnpageNo.getId()) {
            InputNum();
        }
    }

    private void InputNum() {
        final EditText inputNo = new EditText(this);
        inputNo.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入").setIcon(android.R.drawable.ic_dialog_info).setView(inputNo).setNegativeButton("取消", null);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Integer inputPageNo = Integer.valueOf(inputNo.getText().toString());
                        mImgViewManage.GotoNumPic(inputPageNo);
                        btnpageNo.setText("页码(" + mImgViewManage.mCurrPos + ")");
                    }
                });
        builder.show();
    }
}
