package com.example.sharon.beagroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import org.w3c.dom.Text;

import java.util.UUID;

public class showBeacons extends AppCompatActivity {


    private BeaconManager beaconManager;
    private BeaconRegion region;
    private Beacon minBeacon;
    private String myLocation;
    //uploadLocation upload = new uploadLocation();

    //private Button startService;
    //private Button stopSerivice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_beacons);

        //2018.10.14 background service testing -> periodicallyUploadService.class
        //scanning();

        //startService = (Button)findViewById(R.id.button_startService);
        //stopSerivice = (Button)findViewById(R.id.button_stopService);


        /**
         EstimoteSDK.initialize(showBeacons.this, "beagroup-oya","d1ce59f30dae0f0457e60e376503549a");
         EstimoteSDK.enableDebugLogging(true);

         MyApplication myApplication = (MyApplication)getApplicationContext();
         SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(showBeacons.this);
         String currentUser = preferences.getString("USER","null");
         //final TextView B1_output = (TextView) findViewById(R.id.B1);
         //final TextView B2_output = (TextView) findViewById(R.id.B2);
         //final TextView B3_output = (TextView) findViewById(R.id.B3);
         final TextView location_output = (TextView) findViewById(R.id.location);

         beaconManager = new BeaconManager(this);
         beaconManager.setForegroundScanPeriod(800,4000);
         region =  new BeaconRegion("regione", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
         beaconManager.setRangingListener((BeaconManager.BeaconRangingListener) (region, list) -> {
         if (list.size()!=0) {

         minBeacon = list.get(0);
         for (int i=0; i<list.size(); i++){

         if (list.get(i).getRssi()>minBeacon.getRssi()){
         minBeacon = list.get(i);
         }
         }

         switch (minBeacon.getMajor()){
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
         Toast.makeText(getApplicationContext(), "Unrecognized UUID", Toast.LENGTH_LONG).show();
         break;
         }
         //Toast.makeText(getApplicationContext(), myLocation, Toast.LENGTH_LONG).show();
         }else{
         Toast.makeText(getApplicationContext(), ((String)("No Beacon nearby...")), Toast.LENGTH_LONG).show();
         }

         //displayScanediBeacons(B1_output, myApplication.B1);
         //displayScanediBeacons(B2_output, myApplication.B2);
         //displayScanediBeacons(B3_output, myApplication.B3);
         myApplication.userLocation = myLocation;
         location_output.setText("Your are near Estimote - "+ myLocation);

         //upload currentUser's location
         //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(showBeacons.this);
         //String currentUser = preferences.getString("USER","null");
         //Toast.makeText(getApplicationContext(), currentUser, Toast.LENGTH_LONG).show();
         //onPause();
         //upload.execute(currentUser, myApplication.userLocation);
         //onResume();
         });

         **/
    }

    /**
     protected void onResume() {
     super.onResume();
     SystemRequirementsChecker.checkWithDefaultDialogs(this);
     beaconManager.connect(() -> beaconManager.startRanging(region));
     }


     @Override
     protected void onPause(){
     beaconManager.stopRanging(region);
     super.onPause();
     }
     **/

    public void displayScanediBeacons(TextView Beacon_info, Beacon ibeacon) {

        Beacon_info.setText("MacAddress: "+ibeacon.getMacAddress()+"\n"+
                "Major: "+ibeacon.getMajor()+"\n"+
                "Minor: "+ibeacon.getMinor()+"\n"+
                "RSSI: "+ibeacon.getRssi()+"\n"+
                "TimeStamp: "+ibeacon.getTimestamp());
    }


    public double calculateDistance(int rssi){

        double distance;
        distance = Math.pow(10.0, (10-Math.abs(rssi))/(10*2.5));
        return distance;
    }

    public void stopServiceButton(View view){
        Intent intent_service = new Intent(this, periodicallyUploadService.class);
        stopService(intent_service);
        Toast.makeText(getApplicationContext(), "Stop Service", Toast.LENGTH_LONG).show();
    }

    public void displayMyLocation(View view){
        MyApplication myApplication = (MyApplication)getApplicationContext();
        Toast.makeText(getApplicationContext(), myApplication.userLocation, Toast.LENGTH_LONG).show();
    }

    public void scanning(){
        EstimoteSDK.initialize(showBeacons.this, "beagroup-oya","d1ce59f30dae0f0457e60e376503549a");
        EstimoteSDK.enableDebugLogging(true);

        MyApplication myApplication = (MyApplication)getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(showBeacons.this);
        String currentUser = preferences.getString("USER","null");
        //final TextView B1_output = (TextView) findViewById(R.id.B1);
        //final TextView B2_output = (TextView) findViewById(R.id.B2);
        //final TextView B3_output = (TextView) findViewById(R.id.B3);
        final TextView location_output = (TextView) findViewById(R.id.location);

        beaconManager = new BeaconManager(this);
        beaconManager.setForegroundScanPeriod(800,4000);
        beaconManager.setBackgroundScanPeriod(800,4000);
        region =  new BeaconRegion("regione", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        beaconManager.setRangingListener((BeaconManager.BeaconRangingListener) (region, list) -> {
            if (list.size()!=0) {

                minBeacon = list.get(0);
                for (int i=0; i<list.size(); i++){

                    if (list.get(i).getRssi()>minBeacon.getRssi()){
                        minBeacon = list.get(i);
                    }
                }

                switch (minBeacon.getMajor()){
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
                        Toast.makeText(getApplicationContext(), "Unrecognized UUID", Toast.LENGTH_LONG).show();
                        break;
                }
                //Toast.makeText(getApplicationContext(), myLocation, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), ((String)("No Beacon nearby...")), Toast.LENGTH_LONG).show();
            }

            //displayScanediBeacons(B1_output, myApplication.B1);
            //displayScanediBeacons(B2_output, myApplication.B2);
            //displayScanediBeacons(B3_output, myApplication.B3);
            myApplication.userLocation = myLocation;
            location_output.setText("Your are near Estimote - "+ myApplication.userLocation);

            //upload currentUser's location
            //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(showBeacons.this);
            //String currentUser = preferences.getString("USER","null");
            //Toast.makeText(getApplicationContext(), currentUser, Toast.LENGTH_LONG).show();
            //onPause();
            //upload.execute(currentUser, myApplication.userLocation);
            //onResume();
        });
    }

    public void start_Service(View view){
        Intent startIntent = new Intent(this, periodicallyUploadService.class);
        startService(startIntent);
    }

    public void stop_Service(View view){
        //MyApplication myApplication = (MyApplication)getApplicationContext();
        //Toast.makeText(getApplicationContext(), myApplication.userLocation, Toast.LENGTH_LONG).show();
        Intent stopIntent = new Intent(this, periodicallyUploadService.class);
        stopService(stopIntent);
    }

}
