package com.example.sharon.beagroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    uploadLocation upload = new uploadLocation();
    //2018.09.07
    //private Beacon[] beacon = new Beacon[3];
    //GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();

    //2018.09.06
    //int B1_rssi;
    //final TextView B1_output = (TextView) findViewById(R.id.B1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_beacons);
        EstimoteSDK.initialize(showBeacons.this, "beagroup-oya","d1ce59f30dae0f0457e60e376503549a");
        EstimoteSDK.enableDebugLogging(true);

        GlobalVariable globalVariable = (GlobalVariable)getApplicationContext();
        final TextView B1_output = (TextView) findViewById(R.id.B1);
        final TextView B2_output = (TextView) findViewById(R.id.B2);
        final TextView B3_output = (TextView) findViewById(R.id.B3);
        final TextView location_output = (TextView) findViewById(R.id.location);

        beaconManager = new BeaconManager(this);
        beaconManager.setForegroundScanPeriod(800,4000);
        region =  new BeaconRegion("regione", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
        beaconManager.setRangingListener((BeaconManager.BeaconRangingListener) (region, list) -> {
            if (list.size()!=0) {

                //2018.09.06
                //B1_rssi=list.get(0).getRssi();
                //B1.setText("Major: "+B1_rssi);

                //2018.09.07 " succeed in scanning ibeacon "
                //Toast.makeText(getApplicationContext(), ((String)(""+list.size())), Toast.LENGTH_LONG).show();
                //onPause();
                //beacon[0]=list.get(0);
                //beacon[1]=list.get(1);
                //beacon[2]=list.get(2);

                //2018.09.08 " random ibeacon data binded "
                //globalVariable.B1=list.get(0);
                //globalVariable.B2=list.get(1);
                //globalVariable.B3=list.get(2);
                //B1_output.setText("Major: "+globalVariable.B1.getMajor());

                /**2018.09.07**/
                for (int i=0; i<list.size(); i++){
                    switch (list.get(i).getMajor()){
                        case 14346:
                            //B1_output.setText("Major: "+globalVariable.B1.getMajor()+"\n"+"Minor: "+globalVariable.B1.getMinor());
                            globalVariable.B1=list.get(i);
                            break;
                        case 65251:
                            globalVariable.B2=list.get(i);
                            break;
                        case 34469:
                            globalVariable.B3=list.get(i);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Unregonized UUID", Toast.LENGTH_LONG).show();
                            break;
                    }
                }

                //}else{
                Toast.makeText(getApplicationContext(), ((String)(""+list.size())), Toast.LENGTH_LONG).show();
            }
            displayScanediBeacons(B1_output, globalVariable.B1);
            displayScanediBeacons(B2_output, globalVariable.B2);
            displayScanediBeacons(B3_output, globalVariable.B3);
            globalVariable.userLocation = userLocation(globalVariable.B1.getRssi(), globalVariable.B2.getRssi(), globalVariable.B3.getRssi());
            location_output.setText("Your are near Estimote - "+ globalVariable.userLocation);
            upload.execute("test3", globalVariable.userLocation);
            onPause();
            //2018.09.08 "show complete ibeacon info"
            /**
             B1_output.setText(  ""+globalVariable.B1.getProximityUUID().toString().toUpperCase()+"\n"+
             "MacAddress: "+globalVariable.B1.getMacAddress()+"\n"+
             "Major: "+globalVariable.B1.getMajor()+"\n"+
             "Minor: "+globalVariable.B1.getMinor()+"\n"+
             "RSSI: "+globalVariable.B1.getRssi()+"\n"+
             "MeasuredPower: "+globalVariable.B1.getMeasuredPower()+"\n"+
             "TimeStamp: "+globalVariable.B1.getTimestamp());
             **/
        });

    }

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

    public void displayScanediBeacons(TextView Beacon_info, Beacon ibeacon) {

        //2018.09.15
        //double distance;
        //distance = calculateDistance(ibeacon.getRssi());

        Beacon_info.setText("MacAddress: "+ibeacon.getMacAddress()+"\n"+
                "Major: "+ibeacon.getMajor()+"\n"+
                "Minor: "+ibeacon.getMinor()+"\n"+
                "RSSI: "+ibeacon.getRssi()+"\n"+
                //"Distance: "+distance+" m\n"+
                //"MeasuredPower: "+ibeacon.getMeasuredPower()+"\n"+
                "TimeStamp: "+ibeacon.getTimestamp());
    }

    public double calculateDistance(int rssi){

        //2018.09.15 'beacon signal is quit unstable and we will get a lot of jumping of values'
        //Ref:
        //Weighted Lesat Squares Techinques for Improved Received Signal Strength Based Localization
        //https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3231493/

        double distance;
        distance = Math.pow(10.0, (10-Math.abs(rssi))/(10*2.5));
        return distance;
    }

    public String userLocation(int b1_rssi, int b2_rssi, int b3_rssi){

        //2018.09.16
        //

        int max;
        max = Math.max(Math.max(b1_rssi, b2_rssi), b3_rssi);

        if (b1_rssi == max) return "Lemon";
        else if (b2_rssi == max) return "Candy";
        else return "Beetroot";
    }
}
