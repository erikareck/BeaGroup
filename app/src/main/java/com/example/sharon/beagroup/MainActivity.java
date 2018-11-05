package com.example.sharon.beagroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Erika 2018.10.15試圖關閉不使用之Activity
    public static MainActivity instance = null;
    public static String userID;
    public static String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Activity OnCreate() order", "MainActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        MyApplication myApplication = (MyApplication) getApplicationContext();

        /**Erika 2018.10.15 Close Activity**/
        instance = this;
        /**Erika 2018.10.16 Start Background Scanning Service**/
        /**Erika 2018.10.18 Recode Background Scanning service a class method**/
        startScanningService();

        if(!SaveSharedPreference.getLog(MainActivity.this))
        {
            //未登入狀態跳到login

            // call Login Activity
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }


    }
    public void openAccount(View view){
        startActivity(new Intent(this, account.class));
    } //按下個人帳戶進入畫面
    public void showMyLocation(View view){
        Intent intent = new Intent(this, showBeacons.class);
        startActivity(intent);
    }

    public void openLock(View view){
        startActivity(new Intent(this, lockFriend.class));
    } //按下個人帳戶進入畫面

    public void findMerchandise(View view){

        MyApplication myApplication = (MyApplication) getApplicationContext();
        startActivity(new Intent(this, findMerchandise_furniture.class));
        /**
        if (myApplication.userLocation == "Furniture") {
            //Log.d("MainActivity:findMerchandise", ""+myApplication.userLocation);
            startActivity(new Intent(this, findMerchandise_furniture.class));
        }else if (myApplication.userLocation == "Grocery"){
            startActivity(new Intent(this, findMerchandise_furniture.class));
        }else if (myApplication.userLocation == "Brunch Buffet"){
            startActivity(new Intent(this, findMerchandise_Buffet.class));
        }else{
            //location is unknown ->
        }
         **/
    } //尋找商品

    public void findFriends(View view){
        startActivity(new Intent(this, findFriends.class));
    }
    /**Very Important for Estimote SDK ranging method**/
    protected  void onResume(){
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        startScanningService();

        userID = SaveSharedPreference.getID(this);
        groupID = SaveSharedPreference.getGroup_ID(this);


        if (userID != "null"){
            if (groupID == "null"){
                //產生Group_ID token (隨機)
                groupID = groupIDGenerator();
                SaveSharedPreference.setGroup_ID(MainActivity.this, groupID);
                Log.d("group_id","give "+ userID+" a Group_ID - "+ SaveSharedPreference.getGroup_ID(this));
                assignGroupID assignGID = new assignGroupID();
                assignGID.execute(userID, groupID);
            }else{
                Log.d("group_id",userID+" already has a Group_ID - "+ groupID);
            }
        }

    }

    public void startScanningService(){
        boolean serviceRunning = serviceUtils.isServiceRunning(this, "com.example.sharon.beagroup.periodicallyUploadService");
        if (serviceRunning){
            Log.d("MainActivity.startScanningService()", "service [periodicallyUploadService] is running");
        }else{
            Log.d("MainActivity.startScanningService()", "service [periodicallyUploadService] is't running");
            Intent startServiceIntent = new Intent(this, periodicallyUploadService.class);
            startService(startServiceIntent);
            Log.d("MainActivity.startScanningService()", "START service [periodicallyUploadService]");
        }

    }

    public String groupIDGenerator(){
        int downCase, numOfChar;
        String token;
        String randomChar = "";
        Random r = new Random();
        numOfChar = (int)(Math.random()*5+1);
        for (int i = 0; i<numOfChar; i++){
            downCase = r.nextInt(26)+97;
            randomChar += String.valueOf((char)downCase);
        }
        //Log.d("token","randomChar: "+ randomChar);
        token = Integer.toString((int)(Math.random()*1000000));
        token = token + randomChar;
        Log.d("token","token before shuffle: "+token);
        String [] strList = token.split("");
        List<String> charList = new ArrayList<String>(Arrays.asList(strList));
        charList.remove(0);
        Collections.shuffle(charList);
        StringBuilder builder = new StringBuilder();
        for (String s : charList){
            builder.append(s);

        }
        token = builder.toString();
        Log.d("token","token after shuffle: "+ token);

        return token;
    }
}
