package com.example.sharon.beagroup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
    private String lastLocation;

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

            if (!myLocation.equals(lastLocation)) {
                upload.execute(currentUser, myApplication.userLocation);
                Log.d(TAG, "UserLocation has been changed, upload.execute()");

                switch (myLocation){
                    case "Food Court"://buffet
                        showNotification("2019 生｜活","You are near Food Court", R.drawable.barbecue, findMerchandise_Buffet.class);
                        break;
                    case "Grocery"://grocery
                        showNotification("布朗尼免費教學","You are near grocery", R.drawable.fancycrave, findMerchandise_Grocery.class);
                        break;
                    case "Furniture"://furniture
                        showNotification("2019 生｜活","You are near furniture", R.drawable.toaheftiba, findMerchandise_furniture.class);
                        break;
                }
            }
        }
    };
    Timer timer = new Timer();

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        lastLocation = "startAPP";
        scanning();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        timer.schedule(action, 10000, 5000);
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
                lastLocation = myLocation;
                switch (minBeacon.getMajor()) {

                    case 14346:
                        myLocation = "Food Court";
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
            Log.d(TAG,"assign userlocation: " + myLocation);
            Log.d(TAG, "lastLocation: " + lastLocation);
            //upload currentUser's location
            //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(showBeacons.this);
            //String currentUser = preferences.getString("USER","null");
            //Toast.makeText(getApplicationContext(), myApplication.userLocation, Toast.LENGTH_LONG).show();

        });
    }

    public void showNotification(String title, String message, int pictureID, Class activityClass) {
        Bitmap pic = BitmapFactory.decodeResource(getResources(), pictureID);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        Intent advertisement = new Intent(this, activityClass);
            //findMerchandise_furniture.class 因為圖片有版權問題不放資料庫(僅限demo使用)
            //findMerchandise_grocery.class
            //findMerchandise_buffet.class

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                //new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
                new Intent[] { advertisement }, PendingIntent.FLAG_UPDATE_CURRENT);//打開APP尋找商品的頁面
        Notification notification = new Notification.Builder(this)
                //.setSmallIcon(android.R.drawable.ic_dialog_info)
                //.setColor(Color.argb(1, 244, 92, 47))
                .setColor(Color.rgb(244, 92, 47))
                .setSmallIcon(R.drawable.baseline_shopping_basket_black_48)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new Notification.BigPictureStyle().bigPicture(pic))
                //.addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent)
                //.addAction(android.R.drawable.ic_delete, "DISMISS", )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);


    }
}