package com.mx.android.wmapp.ImgView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

/**
 * Created by Administrator on 2017-01-11.
 */
public class ImgViewManage {
    public Integer mCurrPos;
    private Context mContext;
    private ZoomImageView mZoomImg;
    private Integer mViewType = 0;//0文件浏览，1目录浏览
    private String mCurrPath;
    private List<String> mAllFileList;
    private List<ZipEntry> mAllImgList;
    private ZipFile mZipFile;
    private ZipInputStream mZipInputStream;

    public ImgViewManage(Context context, ZoomImageView zoomImageView) {
        mContext = context;
        mAllImgList = new ArrayList<ZipEntry>();
        mZoomImg = zoomImageView;
    }

    public void NextPic() {
        if (mViewType == 0) {
            if (mCurrPos.intValue() < mAllImgList.size() - 1) {
                mCurrPos = mCurrPos + 1;
                SetBitmapFormZipEntry(mCurrPos);
            } else {
                ZipEntry ze = GetNextZE();
                if (ze != null) {
                    mAllImgList.add(ze);
                    SetBitmapFormZipEntry(mAllImgList.size() - 1);
                }
            }
        } else {
            if (mCurrPos.intValue() < mAllImgList.size() - 1) {
                mCurrPos = mCurrPos + 1;
                SetBitmapFormFileName(mCurrPos);
            }
        }
    }

    public void BeforePic() {
        if (mViewType == 0) {
            if (mCurrPos.intValue() > 0) {
                mCurrPos = mCurrPos - 1;
                SetBitmapFormZipEntry(mCurrPos);
            }
        } else {
            if (mCurrPos.intValue() > 0) {
                mCurrPos = mCurrPos - 1;
                SetBitmapFormFileName(mCurrPos);
            }
        }
    }

    public void GotoNumPic(Integer num) {
        if (num.intValue() < mAllImgList.size()) {
            SetBitmapFormZipEntry(num);
        } else {
            while (num.intValue() != mAllImgList.size()) {
                ZipEntry ze = GetNextZE();
                if (ze == null) {
                    break;
                }
                mAllImgList.add(ze);
            }
            SetBitmapFormZipEntry(mAllImgList.size() - 1);
        }
    }

    public void IniImgList(String filePath, Integer viewType) {
        mCurrPath = filePath;
        mViewType = viewType;
        if (mViewType.intValue() == 0) {
            try {
                readZipFile(filePath);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            mAllFileList = getFiles(filePath);
            if (mAllFileList.size() > 0) {
                mCurrPos = 0;
                Bitmap bitmap = BitmapFactory.decodeFile(mAllFileList.get(mCurrPos));
                mZoomImg.setImage(bitmap);
            }
        }
    }

    protected void readZipFile(String file) throws Exception {
        mZipFile = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        mZipInputStream = new ZipInputStream(in);
        ZipEntry ze = GetNextZE();
        if (ze != null) {
            mAllImgList.add(ze);
            SetBitmapFormZipEntry(mAllImgList.size() - 1);
        }
    }

    protected ZipEntry GetNextZE() {
        ZipEntry ze = null;
        try {
            while ((ze = mZipInputStream.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    long size = ze.getSize();
                    if (size > 0) {
                        break;
                    }
                }
            }
            if (ze == null) {
                mZipInputStream.closeEntry();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ze;
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

    protected void SetBitmapFormZipEntry(Integer readPos) {
        try {
            ZipEntry ze = mAllImgList.get(readPos);
            Bitmap bitmap = BitmapFactory.decodeStream(mZipFile.getInputStream(ze));
            Bitmap oldbitmap = mZoomImg.getmBitmap();
            mZoomImg.setImage(bitmap);
            oldbitmap.recycle();
            mCurrPos = readPos;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void SetBitmapFormFileName(Integer readPos) {
        String readName = mAllFileList.get(readPos);
        Bitmap bitmap = BitmapFactory.decodeFile(readName);
        Bitmap oldbitmap = mZoomImg.getmBitmap();
        mZoomImg.setImage(bitmap);
        oldbitmap.recycle();
        mCurrPos = readPos;
    }
}
