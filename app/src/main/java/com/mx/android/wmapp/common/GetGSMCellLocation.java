package com.mx.android.wmapp.common;

import android.os.Handler;
import android.os.Message;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/** 
 * 功能描述：通过手机信号获取基站信息 
 * # 通过TelephonyManager 获取lac:mcc:mnc:cell-id 
 * # MCC，Mobile Country Code，移动国家代码（中国的为460）； 
 * # MNC，Mobile Network Code，移动网络号码（中国移动为0，中国联通为1，中国电信为2）；  
 * # LAC，Location Area Code，位置区域码； 
 * # CID，Cell Identity，基站编号； 
 * # BSSS，Base station signal strength，基站信号强度。 
 * @author android_ls 
 */  
public class GetGSMCellLocation {
	public static final int REFRESH = 0x000001;  
	
	private TelephonyManager mTelephonyManager = null;
	private TextView mInfoList = null;

	private int mcc = 0;  
	private int mnc = 0;   
	private int lac = 0;  
	private int cid  = 0; 
	private String mAddress  = ""; 
	private Handler mHandler = null;  
	private String aInfo = "";
    
	public GetGSMCellLocation(TelephonyManager aTM, TextView aTV){
		mTelephonyManager = aTM;
		mInfoList = aTV;
	}
	
	public String GSMCellInfo(){
        // 返回值MCC + MNC  
        String operator = mTelephonyManager.getNetworkOperator();  
        mcc = Integer.parseInt(operator.substring(0, 3));  
        mnc = Integer.parseInt(operator.substring(3));  

        // 中国移动和中国联通获取LAC、CID的方式  
        GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getCellLocation();  
        lac = location.getLac();  
        cid  = location.getCid();  
        
        aInfo = "当前连接基站 \n MNC = " + mnc + "\t LAC = " + lac + "\t CID = " + cid ; 
//        Log.i(Thread.currentThread().getName(), " MCC = " + mcc + "\t MNC = " + mnc + "\t LAC = " + lac + "\t CID = " + cid ); 
     // 获取邻区基站信息  
        List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();  
        StringBuffer sb = new StringBuffer("总数 : " + infos.size() + "\n");  
        for (NeighboringCellInfo info1 : infos) { // 根据邻区总数进行循环  
            sb.append(" LAC : " + info1.getLac()); // 取出当前邻区的LAC  
            sb.append(" CID : " + info1.getCid()); // 取出当前邻区的CID  
            sb.append(" BSSS : " + (-113 + 2 * info1.getRssi()) + "\n"); // 获取邻区基站信号强度  
        }  
 
        aInfo += " 获取邻区基站信息:" + sb.toString();
        Log.i(Thread.currentThread().getName(), aInfo);  
        
        mHandler = new Handler() {  
            @Override  
            public void handleMessage(Message msg) {  
                if (msg.what == REFRESH) {  
                	Log.i(Thread.currentThread().getName(), "handleMessage = " + aInfo);  
                    mInfoList.setText(aInfo);
                }  
                super.handleMessage(msg);  
            }  
        };  
        new Thread() {  
            @Override  
            public void run() {  
                try {  
                    String json = getJsonCellPos(mcc, mnc, lac, cid);  
                    Log.i(Thread.currentThread().getName(), "request = " + json);  

                    String url = "http://www.minigps.net/minigps/map/google/location";  
                    mAddress = "地址：" + httpPost(url, json);  
                    Log.i(Thread.currentThread().getName(), mAddress);  
                    aInfo += mAddress;
                    Message msg = new Message();  
                    msg.what = REFRESH;  
                    mHandler.sendMessage(msg);   
                  //return aInfo;
                } catch (Exception e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
        }.start(); 
        aInfo += mAddress;
        return aInfo;
	}
	
	/** 
     * 调用第三方公开的API根据基站信息查找基站的经纬度值及地址信息 
     */  
    public String httpPost(String url, String jsonCellPos) throws IOException{  
        byte[] data = jsonCellPos.toString().getBytes();  
        URL realUrl = new URL(url);  
        HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();  
        httpURLConnection.setConnectTimeout(6 * 1000);  
        httpURLConnection.setDoOutput(true);  
        httpURLConnection.setDoInput(true);  
        httpURLConnection.setUseCaches(false);  
        httpURLConnection.setRequestMethod("POST");  
        httpURLConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");  
        httpURLConnection.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");  
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");  
        httpURLConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");  
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");  
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));  
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");  
  
        httpURLConnection.setRequestProperty("Host", "www.minigps.net");  
        httpURLConnection.setRequestProperty("Referer", "http://www.minigps.net/map.html");  
        httpURLConnection.setRequestProperty("User-Agent",  
                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4X-Requested-With:XMLHttpRequest");  
  
        httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");  
        httpURLConnection.setRequestProperty("Host", "www.minigps.net");  
  
        DataOutputStream outStream = new DataOutputStream(httpURLConnection.getOutputStream());  
        outStream.write(data);  
        outStream.flush();  
        outStream.close();  
        Log.i(Thread.currentThread().getName(), "getResponseCode=begin");
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) { 
        	Log.i(Thread.currentThread().getName(), "HttpURLConnection.HTTP_OK"); 
            InputStream inputStream = httpURLConnection.getInputStream();  
            return new String(read(inputStream));  
        }  
        Log.i(Thread.currentThread().getName(), "getResponseCode()=" + httpURLConnection.getResponseCode());
        return null;  
    }  
    /**  
     * 获取JSON形式的基站信息  
     * @param mcc 移动国家代码（中国的为460）  
     * @param mnc 移动网络号码（中国移动为0，中国联通为1，中国电信为2）；   
     * @param lac 位置区域码  
     * @param cid 基站编号  
     * @return json  
     * @throws JSONException  
     */  
    private String getJsonCellPos(int mcc, int mnc, int lac, int cid) throws JSONException {  
        JSONObject jsonCellPos = new JSONObject();  
        jsonCellPos.put("version", "1.1.0");  
        jsonCellPos.put("host", "maps.google.com");  
  
        JSONArray array = new JSONArray();  
        JSONObject json1 = new JSONObject();  
        json1.put("location_area_code", "" + lac + "");  
        json1.put("mobile_country_code", "" + mcc + "");  
        json1.put("mobile_network_code", "" + mnc + "");  
        json1.put("age", 0);  
        json1.put("cell_id", "" + cid + "");  
        array.put(json1);  
  
        jsonCellPos.put("cell_towers", array);  
        return jsonCellPos.toString();  
    }  
  
    /** 
     * 读取IO流并以byte[]形式存储 
     * @param inputSream InputStream 
     * @return byte[] 
     * @throws IOException 
     */  
    public byte[] read(InputStream inputSream) throws IOException {  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        int len = -1;  
        byte[] buffer = new byte[1024];  
        while ((len = inputSream.read(buffer)) != -1) {  
            outStream.write(buffer, 0, len);  
        }  
        outStream.close();  
        inputSream.close();  
  
        return outStream.toByteArray();  
    }  
}
