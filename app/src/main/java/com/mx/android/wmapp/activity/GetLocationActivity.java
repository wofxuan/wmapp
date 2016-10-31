package com.mx.android.wmapp.activity;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.mx.wmapp.R;
import com.mx.android.wmapp.base.BaseActivity;
import com.mx.android.wmapp.common.GetGSMCellLocation;
import com.mx.android.wmapp.common.LBSTool;
import com.mx.android.wmapp.common.LocationData;

public class GetLocationActivity extends BaseActivity {
    private TextView locationInfoTextView = null;
    private Button startButton = null;
//    private com.android.common.LBBDSDK aBDSDK = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        locationInfoTextView = (TextView) this.findViewById(R.id.Location);
        locationInfoTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        Button startGPS = (Button) this.findViewById(R.id.btn_startGPS);
        startGPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                useGPSORNETWORK(LocationManager.GPS_PROVIDER);
            }
        });

        Button startNET = (Button) this.findViewById(R.id.btn_startNET);
        startNET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                useGPSORNETWORK(LocationManager.NETWORK_PROVIDER);
            }
        });

        Button getGSMCell = (Button) this.findViewById(R.id.btn_GetGSMCell);
        getGSMCell.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                GetGSMCellLocation mGetGSMCell = new GetGSMCellLocation(mTelephonyManager, locationInfoTextView);
                locationInfoTextView.setText(mGetGSMCell.GSMCellInfo());
            }
        });
    }

    //使用GPS或者网络访问
    protected void useGPSORNETWORK(String AType) {
        locationInfoTextView = (TextView) this.findViewById(R.id.Location);
        StringBuffer sb = new StringBuffer(256);
        LBSTool lbs = new LBSTool(GetLocationActivity.this, AType);
        LocationData location = lbs.getLocation(120000);
        if (location != null) {
            sb.append("lat:" + location.lat + "\n");
            sb.append("lon:" + location.lon + "\n");
            sb.append("address:" + location.address);
        } else {
            sb.append("location = null");
        }
        locationInfoTextView.setText(sb.toString());
    }
}
