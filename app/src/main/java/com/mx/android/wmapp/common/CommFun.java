package com.mx.android.wmapp.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.android.mx.wmapp.R;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016-06-07.
 */
public class CommFun {

    // 获得总内存
    public static String getTotalMemory(@Nullable Context contex) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(contex, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    //获取所有包的信息
    public static String getPackageList(@Nullable Context contex) {
        String res = "";
        PackageManager pManager = contex.getPackageManager();
        List<PackageInfo> appList = pManager.getInstalledPackages(0);

        for (int i = 0; i < appList.size(); i++) {
            PackageInfo pinfo = appList.get(i);
            //判断是否为非系统预装的应用程序
            if ((pinfo.applicationInfo.flags & pinfo.applicationInfo.FLAG_SYSTEM) <= 0) {
                res += pinfo.applicationInfo.packageName + " \n";
                res += pManager.getApplicationLabel(pinfo.applicationInfo).toString() + " \n";
            }
        }
        return res;
    }

    // 获得总内存
    public static void showPackageList(@Nullable Context contex) {
        final AlertDialog mxalertDialog = new AlertDialog.Builder(contex).create();
        mxalertDialog.show();
        Window window = mxalertDialog.getWindow();
        window.setContentView(R.layout.mxdialog);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
        tv_title.setText("详细信息");
        TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
        tv_message.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_message.setText(CommFun.getPackageList(contex));

        mxalertDialog.getWindow().findViewById(R.id.button_back_mydialog)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mxalertDialog.dismiss();
                    }
                });
    }
}
