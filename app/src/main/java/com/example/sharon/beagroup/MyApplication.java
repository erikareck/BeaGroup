package com.example.sharon.beagroup;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class MyApplication extends Application {
    public Beacon B1;//14346: Brunch Buffet
    public Beacon B2;//65251: Furniture
    public Beacon B3;//34469: Grocery
    public String userLocation; //user's location
    public String friendCheck;

    private BeaconManager GlobalBeaconManager;

    @Override
    public void onCreate() {
        super.onCreate();
        userLocation = "unknown";
        friendCheck = "";

        //2018.10.24 advertise
        GlobalBeaconManager = new BeaconManager(getApplicationContext());
        GlobalBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                GlobalBeaconManager.startMonitoring(new BeaconRegion(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null
                ));
            }
        });

        GlobalBeaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {

                Log.d("MyApplication: List<Beacon> beacons", "beacons.size() = "+ beacons.size());
                Log.d("MyApplication: List<Beacon> beacons", "beacons.getMajor() = "+ beacons.get(0).getMajor());
                switch (beacons.get(0).getMajor()){
                    case 14346://buffet
                        showNotification("燒烤周 12/18~12/25","You are near buffet", R.drawable.barbecue);
                        break;
                    case 34469://grocery
                        showNotification("布朗尼免費教學","You are near grocery", R.drawable.toaheftiba);
                        break;
                    case 65251://furniture
                        showNotification("鮮活空間企劃","You are near furniture", R.drawable.fancycrave);
                        break;
                }
            }

            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
                //showNotification("goodbye", "Your are leaving Beacon 14346: Buffet");
            }
        });
    }

    public void setFriendCheck(String flag){
        this.friendCheck = flag;
    }
    public String getFriendCheck(){
        return friendCheck;
    }

    public void showNotification(String title, String message, int pictureID) {
        Bitmap pic = BitmapFactory.decodeResource(getResources(), pictureID);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        Intent advertisement = new Intent(this, showBeacons.class);//改寫成廣告頁面.class即可
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