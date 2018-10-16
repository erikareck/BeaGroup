package com.example.sharon.beagroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

public class MainActivity extends AppCompatActivity {

    //Erika 2018.10.15試圖關閉不使用之Activity
    public static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Erika 2018.10.15試圖關閉不使用之Activity
        instance = this;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        /**Start Background Service**/
        Intent startServiceIntent = new Intent(this, periodicallyUploadService.class);
        startService(startServiceIntent);

        if(!SaveSharedPreference.getLog(MainActivity.this))
        {
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

    /**Very Important for Estimote SDK ranging method**/
    protected  void onResume(){
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }
}
