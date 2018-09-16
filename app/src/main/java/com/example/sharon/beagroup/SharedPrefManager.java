package com.example.sharon.beagroup;

import android.content.Context;
import android.net.http.RequestQueue;


public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mContext;

    private SharedPrefManager(Context context){
        mContext = context;
        //mRequest
    }
}
