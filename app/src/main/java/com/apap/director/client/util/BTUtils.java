package com.apap.director.client.util;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.apap.director.client.R;

import java.util.List;

/**
 * Created by Adam on 2016-12-13.
 */

public class BTUtils {
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
}
