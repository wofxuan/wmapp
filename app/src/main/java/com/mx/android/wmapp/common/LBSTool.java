package com.mx.android.wmapp.common;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;

public class LBSTool {
    private Context mContext;  
    private LocationManager mLocationManager;   
    private LocationData mLocation;
    private LBSThread mLBSThread;  
    private MyLocationListner mNetworkListner;  
    private MyLocationListner mGPSListener;  
    private Looper mLooper;  
    private String mType;  
      
    public LBSTool(Context context, String AType) {  
        mContext = context;  
        mType = AType;
        //获取Location manager  
        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);  
    } 
    
    public LocationData getLocation(long timeout) {  
        mLocation = null;  
        mLBSThread = new LBSThread();  
        mLBSThread.start();//启动LBSThread  
        timeout = timeout > 0 ? timeout : 0;  
          
        synchronized (mLBSThread) {  
            try {  
                Log.i(Thread.currentThread().getName(), "Waiting for LocationThread to complete...");  
                mLBSThread.wait(timeout);//主线程进入等待，等待时长timeout ms  
                Log.i(Thread.currentThread().getName(), "Completed.Now back to main thread");  
            }  
            catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
        mLBSThread = null;  
        return mLocation;  
    }
    
    private class LBSThread extends Thread {  
        @Override  
        public void run() {  
            setName("location thread");   
            Log.i(Thread.currentThread().getName(), "--start--");   
            Looper.prepare();//给LBSThread加上Looper  
            mLooper = Looper.myLooper();  
            registerLocationListener();  
            Looper.loop();  
            Log.e(Thread.currentThread().getName(),  "--end--");  
              
        }  
    }  
      
    private void registerLocationListener () {  
        Log.i(Thread.currentThread().getName(), "registerLocationListener");  
    	//获取设备支持哪些定位功能
    	List<String> list=mLocationManager.getAllProviders();  
    	for(String p:list)  
    	{  
    		Log.i(Thread.currentThread().getName(), p); 
    	} 
    	if (mType.equals(LocationManager.GPS_PROVIDER)){
          if (isGPSEnabled()) { 
        	mGPSListener=new MyLocationListner();      
          //五个参数分别为位置服务的提供者，最短通知时间间隔，最小位置变化，listener，listener所在消息队列的looper  
        	mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mGPSListener, mLooper);  
          }else{
        	  synchronized (mLBSThread) {
        		  mLBSThread.notify();//通知主线程继续  
        	  }
          }
    	}else if (mType.equals(LocationManager.NETWORK_PROVIDER)){
    	   if (isNETWORKEnabled()) { 
        	mNetworkListner = new MyLocationListner();   	 
        	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, mNetworkListner, mLooper);     
    	   }else{
    		   synchronized (mLBSThread) {
    			   mLBSThread.notify();//通知主线程继续  
    		   }
    	   }
    	}
//        if (isGPSEnabled()) {  
//        	  mGPSListener=new MyLocationListner();      
//            //五个参数分别为位置服务的提供者，最短通知时间间隔，最小位置变化，listener，listener所在消息队列的looper  
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mGPSListener, mLooper);    
//            Log.i(Thread.currentThread().getName(), "requestLocationUpdates:end");
//        }  
//        if (isNetworkEnabled()) {  
//        	mNetworkListner=new MyLocationListner();   	 
//        	mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, mNetworkListner, mLooper); 
//          Log.i(Thread.currentThread().getName(), "requestLocationUpdates:end");
//        }  
    } 
    
    /**  
     * 判断GPS是否开启  
     * @return  
     */  
    public boolean isGPSEnabled() {  
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  
            Log.i(Thread.currentThread().getName(), "isGPSEnabled");  
            return true;  
        }   
        else {  
            return false;  
        }  
    }  
      
    /**  
     * 判断NETWORK是否开启  
     * @return  
     */  
    public boolean isNETWORKEnabled() {  
        if(mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {  
            Log.i(Thread.currentThread().getName(), "isNETWORKEnabled");  
            return true;  
        }   
        else {  
            return false;  
        }  
    }  
    /**  
     * 判断Network是否开启(包括移动网络和wifi)  
     * @return  
     */  
    public boolean isNetworkEnabled() {  
        return (isWIFIEnabled() || isTelephonyEnabled());   
    }  
      
    /**  
     * 判断移动网络是否开启  
     * @return  
     */  
    public boolean isTelephonyEnabled() {  
        boolean enable = false;  
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);  
        if (telephonyManager != null) {  
            if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {  
                enable = true;  
                Log.i(Thread.currentThread().getName(), "isTelephonyEnabled");  
            }  
        }  
          
        return enable;  
    }  
      
    /**  
     * 判断wifi是否开启  
     */  
    public boolean isWIFIEnabled() {  
        boolean enable = false;  
        WifiManager wifiManager = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);  
        if(wifiManager.isWifiEnabled()) {  
            enable = true;  
            Log.i(Thread.currentThread().getName(), "isWIFIEnabled");   
        }   
        return enable;  
    } 
    
    private class MyLocationListner implements LocationListener{    
    	  
        @Override  
        public void onLocationChanged(Location location) {    
            // 当LocationManager检测到最小位置变化时，就会回调到这里  
            Log.i(Thread.currentThread().getName(), "Got New Location of provider:"+location.getProvider());  
            unRegisterLocationListener();//停止LocationManager的工作  
            try {  
                synchronized (mLBSThread) {   
                    parseLatLon(location.getLatitude()+"", location.getLongitude()+"");//解析地理位置  
                    mLooper.quit();//解除LBSThread的Looper，LBSThread结束  
                    mLBSThread.notify();//通知主线程继续  
                }  
            }  
            catch (Exception e) {  
                e.printStackTrace();  
            }  
        }    
     
        //后3个方法此处不做处理 
        @Override  
        public void onStatusChanged(String provider, int status, Bundle extras) {
        	Log.i(Thread.currentThread().getName(), "onStatusChanged"); 
        }    
     
        @Override  
        public void onProviderEnabled(String provider) {
        	Log.i(Thread.currentThread().getName(), "onProviderEnabled"); 
        }    
     
        @Override  
        public void onProviderDisabled(String provider) {
        	Log.i(Thread.currentThread().getName(), "onProviderDisabled"); 
        }  
      
    };  
      
    /**  
     * 使用经纬度从goole服务器获取对应地址   
     * @param 经纬度  
     */  
    private void parseLatLon(String lat, String lon) throws Exception {  
        Log.e(Thread.currentThread().getName(),  "---parseLatLon---");  
        Log.e(Thread.currentThread().getName(),  "---"+lat+"---");  
        try {  
            mLocation = new LocationData();  
            mLocation.lat = lat;  
            mLocation.lon = lon; 
            mLocation.address = "测试地址"; 
//            HttpClient httpClient = new DefaultHttpClient();  
//            HttpGet get = new HttpGet("http://ditu.google.cn/maps/geo?output=json&q="+lat+","+lon);  
//            HttpResponse response = httpClient.execute(get);  
//            String resultString = EntityUtils.toString(response.getEntity());  
//              
//            JSONObject jsonresult = new JSONObject(resultString);  
//            if(jsonresult.optJSONArray("Placemark") != null) {   
//                mLocation.address = jsonresult.optJSONArray("Placemark").optJSONObject(0).optString("address");  
//            }  
        }  
        catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    /**  
     * 注销监听器   
     */  
    private void unRegisterLocationListener () {  
        if(mGPSListener!=null){    
            mLocationManager.removeUpdates(mGPSListener);    
            mGPSListener=null;    
        }   
        if(mNetworkListner!=null){    
            mLocationManager.removeUpdates(mNetworkListner);    
            mNetworkListner=null;    
        }   
    } 
}

