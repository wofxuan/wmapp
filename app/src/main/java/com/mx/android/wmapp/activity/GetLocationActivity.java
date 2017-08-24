package com.mx.android.wmapp.activity;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mx.wmapp.R;
import com.mx.android.wmapp.base.BaseActivity;
import com.mx.android.wmapp.common.GetGSMCellLocation;
import com.mx.android.wmapp.common.LBSTool;
import com.mx.android.wmapp.common.LocationData;
import com.mx.android.wmapp.entity.EventCenter;

import butterknife.BindView;
import butterknife.OnClick;

public class GetLocationActivity extends BaseActivity {
    @BindView(R.id.Location)
    public TextView locationInfoTextView;

    @OnClick({R.id.btn_startGPS, R.id.btn_startNET, R.id.btn_GetGSMCell})
    public void onClick(TextView button) {
        switch (button.getId()) {
            case R.id.btn_startGPS:
                useGPSORNETWORK(LocationManager.GPS_PROVIDER);
                break;
            case R.id.btn_startNET:
                useGPSORNETWORK(LocationManager.NETWORK_PROVIDER);
                break;
            case R.id.btn_GetGSMCell:
                TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                GetGSMCellLocation mGetGSMCell = new GetGSMCellLocation(mTelephonyManager, locationInfoTextView);
                locationInfoTextView.setText(mGetGSMCell.GSMCellInfo());
                break;
            default:
                Toast.makeText(this, "没有处理", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationInfoTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_get_location;
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

    //使用GPS或者网络访问
    protected void useGPSORNETWORK(String AType) {
        locationInfoTextView = (TextView) this.findViewById(R.id.Location);
        StringBuffer sb = new StringBuffer(256);
        LBSTool lbs = new LBSTool(GetLocationActivity.this, AType);
        LocationData location = lbs.getLocation(120000);
        if (location != null) {
            sb.append("纬度:" + location.lat + "\n");
            sb.append("经度:" + location.lon + "\n");
            sb.append("高度:" + location.Bearing + "\n");
            sb.append("address:" + location.address);
        } else {
            sb.append("location = null");
        }
        locationInfoTextView.setText(sb.toString());
    }
}
