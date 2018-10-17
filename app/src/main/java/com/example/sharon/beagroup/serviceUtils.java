package com.example.sharon.beagroup;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class serviceUtils {

    public static boolean isServiceRunning(Context context, String serviceName){

        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(100000);
        for (ActivityManager.RunningServiceInfo info : services){

            String name = info.service.getClassName();
            //Log.d("service class name", name);
            if (serviceName.equals(name)){
                return true;
            }
        }
        return false;
    }
}
