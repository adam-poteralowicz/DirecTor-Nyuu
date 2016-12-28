package com.apap.director.client.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.apap.director.client.R;

import java.util.List;

public class BTUtils {
    public static final int DISCOVER_DURATION = 300;

    public static Intent selectBluetooth(Intent intent, List<ResolveInfo> appsList) {
        if(appsList.size() > 0) {
            String packageName = null;
            String className = null;
            boolean found = false;
            for(ResolveInfo info: appsList){
                packageName = info.activityInfo.packageName;
                if( packageName.equals("com.android.bluetooth")){
                    className = info.activityInfo.name;
                    found = true;
                    break; // found
                }
            }
            if(! found) {
                Log.d("Bluetooth", String.valueOf(R.string.bt_not_found));
                return null;
            }

            intent.setClassName(packageName, className);
        }
        return intent;
    }

    public static Intent getBluetoothDiscoveryPermissionIntent() {
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION );

        return discoveryIntent;
    }
}
