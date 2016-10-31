package com.mx.android.wmapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.mx.wmapp.R;
import com.mx.android.wmapp.base.BaseActivity;
import com.mx.android.wmapp.customview.ZoomImageView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ImgViewActivity extends BaseActivity implements View.OnClickListener {
    private static final int OPEN_FILE_REQUEST_CODE = 1;
    private static final int OPEN_DIR_REQUEST_CODE = 2;
    Context context;
    private Integer viewType = 0;//0文件浏览，1目录浏览
    private String currPath;
    private Integer currPos;
    private List<String> allFileList;
    private List<ZipEntry> allImgList;
    private ZoomImageView zoomImg;
    private Button btnbef;
    private Button btnnext;
    private Button btnpageNo;
    private ZipFile aZipFile;
    private ZipInputStream aZipInputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_view);
        context = this;

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
                        intent.putExtra("defaultDir", getSDPath());
                        intent.putExtra("fileType", new String[]{"*.*"});
                        startActivityForResult(intent, OPEN_FILE_REQUEST_CODE);
                        break;
                    case R.id.imgview_dir_open:
                        intent = new Intent(context, DFSelectActivity.class);
                        intent.putExtra("type", DFSelectActivity.TypeOpen);
                        intent.putExtra("result_code", OPEN_DIR_REQUEST_CODE);
                        intent.putExtra("defaultDir", getSDPath());
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
                R.drawable.ic_launcher);
        zoomImg.setImage(bitmap);

        allImgList = new ArrayList<ZipEntry>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.imgview_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_FILE_REQUEST_CODE) {
            if (resultCode == OPEN_FILE_REQUEST_CODE) {
                Toast.makeText(context, data.getStringExtra("selectPath"), Toast.LENGTH_SHORT).show();
                currPath = data.getStringExtra("selectPath");
                try {
                    readZipFile(currPath);
                    viewType = 0;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (requestCode == OPEN_DIR_REQUEST_CODE) {
            if (resultCode == OPEN_DIR_REQUEST_CODE) {
                currPath = data.getStringExtra("selectPath");
                allFileList = getFiles(currPath);
                if (allFileList.size() > 0) {
                    currPos = 0;
                    Bitmap bitmap = BitmapFactory.decodeFile(allFileList.get(currPos));
                    zoomImg.setImage(bitmap);
                    viewType = 1;
                }
            }
        }
    }

    protected void readZipFile(String file) throws Exception {
        aZipFile = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        aZipInputStream = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = aZipInputStream.getNextEntry()) != null) {
            if (!ze.isDirectory()) {
                long size = ze.getSize();
                if (size > 0) {
                    Bitmap bitmap = BitmapFactory.decodeStream(aZipFile.getInputStream(ze));
                    zoomImg.setImage(bitmap);
                    allImgList.add(ze);
                    currPos = allImgList.size() - 1;
                    break;
                }
            }
        }
//        aZipInputStream.closeEntry();
    }

    private List<String> getFiles(String ipath) {
        List<String> file = new ArrayList<String>();
        File[] myFile = new File(ipath).listFiles();
        if (myFile != null) {
            for (File f : myFile) {
                if (f.isFile()) {
                    file.add(f.toString());
                }
            }
            Collections.sort(file, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.toUpperCase().compareTo(o2.toUpperCase());
                }
            });
        }
        return file;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == btnbef.getId()) {
            if (viewType == 0) {
                if (currPos.intValue() > 0) {
                    currPos = currPos - 1;
                    try {
                        ZipEntry ze = allImgList.get(currPos);
                        Bitmap bitmap = BitmapFactory.decodeStream(aZipFile.getInputStream(ze));
                        Bitmap oldbitmap = zoomImg.getmBitmap();
                        zoomImg.setImage(bitmap);
                        oldbitmap.recycle();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                if (currPos.intValue() > 0) {
                    currPos = currPos - 1;
                    Bitmap bitmap = BitmapFactory.decodeFile(allFileList.get(currPos));
                    Bitmap oldbitmap = zoomImg.getmBitmap();
                    zoomImg.setImage(bitmap);
                    oldbitmap.recycle();
                }
            }
        } else if (v.getId() == btnnext.getId()) {
            if (viewType == 0) {
                if (currPos.intValue() < allImgList.size() - 1) {
                    currPos = currPos + 1;
                    try {
                        ZipEntry ze = allImgList.get(currPos);
                        Bitmap bitmap = BitmapFactory.decodeStream(aZipFile.getInputStream(ze));
                        Bitmap oldbitmap = zoomImg.getmBitmap();
                        zoomImg.setImage(bitmap);
                        oldbitmap.recycle();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    try {
                        ZipEntry ze;
                        while ((ze = aZipInputStream.getNextEntry()) != null) {
                            if (!ze.isDirectory()) {
                                long size = ze.getSize();
                                if (size > 0) {
                                    Bitmap oldbitmap = zoomImg.getmBitmap();
                                    Bitmap bitmap = BitmapFactory.decodeStream(aZipFile.getInputStream(ze));
                                    zoomImg.setImage(bitmap);
                                    allImgList.add(ze);
                                    oldbitmap.recycle();
                                    currPos = allImgList.size() - 1;
                                    break;
                                }
                            }
                        }
                        if (ze == null) {
                            aZipInputStream.closeEntry();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                if (currPos.intValue() < allFileList.size() - 1) {
                    currPos = currPos + 1;
                    Bitmap bitmap = BitmapFactory.decodeFile(allFileList.get(currPos));

                    Bitmap oldbitmap = zoomImg.getmBitmap();
                    zoomImg.setImage(bitmap);
                    oldbitmap.recycle();
                }
            }
        } else if (v.getId() == btnpageNo.getId()) {
            final EditText inputNo = new EditText(this);
            inputNo.setFocusable(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("请输入").setIcon(
                    android.R.drawable.ic_dialog_info).setView(inputNo).setNegativeButton(
                    "取消", null);
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Integer inputPageNo = Integer.valueOf(inputNo.getText().toString());
                            if (inputPageNo.intValue() < allImgList.size()) {
                                try {
                                    ZipEntry ze = allImgList.get(inputPageNo);
                                    Bitmap bitmap = BitmapFactory.decodeStream(aZipFile.getInputStream(ze));
                                    Bitmap oldbitmap = zoomImg.getmBitmap();
                                    zoomImg.setImage(bitmap);
                                    oldbitmap.recycle();
                                    currPos = inputPageNo;
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    ZipEntry ze;
                                    while ((ze = aZipInputStream.getNextEntry()) != null) {
                                        if (!ze.isDirectory()) {
                                            long size = ze.getSize();
                                            if (size > 0) {
                                                allImgList.add(ze);
                                                if (inputPageNo.intValue() == allImgList.size() - 1) {
                                                    Bitmap bitmap = BitmapFactory.decodeStream(aZipFile.getInputStream(ze));
                                                    Bitmap oldbitmap = zoomImg.getmBitmap();
                                                    zoomImg.setImage(bitmap);
                                                    oldbitmap.recycle();
                                                    currPos = allImgList.size() - 1;
                                                    break;
                                                }

                                            }
                                        }
                                    }
                                    if (ze == null) {
                                        aZipInputStream.closeEntry();
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
            builder.show();
        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
