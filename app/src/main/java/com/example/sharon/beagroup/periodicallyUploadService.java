package com.example.sharon.beagroup;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class periodicallyUploadService extends Service {

    //private  static final BeaconRegion ALL_BEACONS = new BeaconRegion("regione", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
    //private static BeaconManager beaconManager;

    private BeaconManager beaconManager;
    private BeaconRegion region;
    private Beacon minBeacon;
    private String myLocation;

    /**NOT SUPPORT API**/
    public static final String TAG = "periodicallyUploadService.service";

    TimerTask action = new TimerTask() {
        @Override
        public void run() {
            MyApplication myApplication = (MyApplication)getApplicationContext();
            uploadLocation upload = new uploadLocation();
            Log.d(TAG, "periodicallyUploadService running....");
            //Log.d(TAG, "userLocation: " + myApplication.userLocation);

            //upload currentUser's location
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String currentUser = preferences.getString("USER","null");
            //Log.d(TAG, currentUser);
            Log.d(TAG, "userID - "+ currentUser+" is near "+ myApplication.userLocation);
            upload.execute(currentUser, myApplication.userLocation);

        }
    };
    Timer timer = new Timer();

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate() executed");

        scanning();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        timer.schedule(action, 10000, 8000);
        return super.onStartCommand(intent, flags, startId);
        //return START_NOT_STICKY;
        //Timer:
        //delay - delay in milliseconds before task is to be executed.
        //period - time in milliseconds between successive task executions.
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        timer.cancel();
        beaconManager.stopRanging(region);
        Log.d(TAG, "onDestroy() executed");
        Log.d(TAG, "stop ranging");
    }

    public void scanning() {



        EstimoteSDK.initialize(getApplicationContext(), "beagroup-oya", "d1ce59f30dae0f0457e60e376503549a");
        EstimoteSDK.enableDebugLogging(true);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //String currentUser = preferences.getString("USER", "null");
        //final TextView location_output = (TextView) findViewById(R.id.location);

        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.setForegroundScanPeriod(800, 4000);
        beaconManager.setBackgroundScanPeriod(800, 4000);
        region = new BeaconRegion("regione", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

        Log.d(TAG, "scanning in periodicallyUploadService");
        beaconManager.setRangingListener((BeaconManager.BeaconRangingListener) (region, list) -> {
            if (list.size() != 0) {

                minBeacon = list.get(0);
                for (int i = 0; i < list.size(); i++) {

                    if (list.get(i).getRssi() > minBeacon.getRssi()) {
                        minBeacon = list.get(i);
                    }
                }

                switch (minBeacon.getMajor()) {
                    case 14346:
                        myLocation = "Brunch Buffet";
                        break;
                    case 65251:
                        myLocation = "Furniture";
                        break;
                    case 34469:
                        myLocation = "Grocery";
                        break;
                    default:
                        myLocation = "unknown";
                        //Toast.makeText(getApplicationContext(), "Unrecognized UUID", Toast.LENGTH_LONG).show();
                        break;
                }
                //Toast.makeText(getApplicationContext(), myLocation, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getApplicationContext(), ((String) ("No Beacon nearby...")), Toast.LENGTH_LONG).show();
            }

            myApplication.userLocation = myLocation;
            Log.d(TAG,"assign userlocation");
            //upload currentUser's location
            //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(showBeacons.this);
            //String currentUser = preferences.getString("USER","null");
            //Toast.makeText(getApplicationContext(), myApplication.userLocation, Toast.LENGTH_LONG).show();

        });
    }
}