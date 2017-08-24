package com.mx.android.wmapp.activity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.mx.wmapp.R;
import com.mx.android.wmapp.base.BaseActivity;
import com.mx.android.wmapp.entity.EventCenter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SysStateActivity extends BaseActivity {
    List<String> aServiceData = null;
    List<String> aRunningTasksData = null;
    List<String> aOtherData = null;
    List<String> aAllData = null;
    String aPackageName = "";
    private ActivityManager mActivityManager = null;

    @BindView(R.id.All_list)
    public ListView mAllListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(info);

        // 系统总内存, 字符类型转换 ，转换成MB格式
        String totalMemSize = getTotalMemory();

        // 系统剩余内存, 字符类型转换 ，转换成MB格式。
        String leftMemSize = Formatter.formatFileSize(getBaseContext(),
                info.availMem);

        aServiceData = new ArrayList<String>();
        aRunningTasksData = new ArrayList<String>();
        aOtherData = new ArrayList<String>();
        aAllData = new ArrayList<String>();
        List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager
                .getRunningServices(30);
//        List<ActivityManager.RunningTaskInfo> runningTasks = mActivityManager
//                .getRunningTasks(30);

        aOtherData.add("系统总内存：" + totalMemSize);
        aOtherData.add("系统剩余内存：" + leftMemSize);
        getServiceClassName(mServiceList, aServiceData);
//        getTaskClassName(runningTasks, aRunningTasksData);

        aAllData.addAll(aOtherData);
//        aAllData.add("获取所有获取到运行中的task（任务）:");
//        aAllData.addAll(aRunningTasksData);
        aAllData.add("获取所有启动的服务的类名  :");
        aAllData.addAll(aServiceData);
        mAllListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.servicelistview, aAllData));
        // mOtherListView.setAdapter(new ArrayAdapter<String>(this,
        // R.layout.servicelistview, aOtherData));
        // mServiceListView.setAdapter(new ArrayAdapter<String>(this,
        // R.layout.servicelistview, aServiceData));
        // mRunningTasksListView.setAdapter(new ArrayAdapter<String>(this,
        // R.layout.servicelistview,
        // aRunningTasksData));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_alarm_clock;
    }


    @Override
    protected boolean isApplyButterKnife() {
        return true;
    }

    @Override
    protected boolean isApplyEventBus() {
        return true;
    }

    @Override
    protected void onEventComing(EventCenter eventCenter) {
    }

    // 获取所有获取到运行中的task（任务）
    private boolean getTaskClassName(
            List<ActivityManager.RunningTaskInfo> mTaskList,
            List<String> mViewData) {
        // String res = "";
        for (ActivityManager.RunningTaskInfo taskInfo : mTaskList) {
            mViewData.add(taskInfo.baseActivity.getPackageName());
            //mViewData.add(taskInfo.baseActivity.getClassName());
        }
        return true;
    }

    // 获得总内存
    private String getTotalMemory() {
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
            initial_memory = Long.valueOf(arrayOfString[1]).longValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    // 获取所有启动的服务的类名
    private boolean getServiceClassName(
            List<ActivityManager.RunningServiceInfo> mServiceList,
            List<String> mViewData) {
        // String res = "";
        for (int i = 0; i < mServiceList.size(); i++) {
            mViewData.add(mServiceList.get(i).service.getPackageName());
            //mViewData.add(mServiceList.get(i).service.getClassName());
        }

        return true;
    }
}
