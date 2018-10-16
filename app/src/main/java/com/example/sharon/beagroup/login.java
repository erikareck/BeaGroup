package com.example.sharon.beagroup;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class login extends AppCompatActivity {

    EditText edUserid, edUserpwd;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get Reference to variables
        edUserid = (EditText) findViewById(R.id.id);
        edUserpwd = (EditText) findViewById(R.id.pwd);

    }

    public void onLogin(View view){

        String userid = edUserid.getText().toString();
        String password = edUserpwd.getText().toString();
        SaveSharedPreference.setID(login.this, userid);
        String type = "login";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type, userid,password); //傳參數(型態：登入、登入內容)
       if(backgroundWork.login_code.equals("1")){ //若登入成功，跳轉至主畫面

           Intent intent = new Intent(getApplicationContext(), MainActivity.class);
           //intent.setClass(login.this, MainActivity.class);
           startActivity(intent);

       }

    }



    public void openSignup(View view){
        startActivity(new Intent(this, signup.class));
    } //按下Signup進入註冊畫面










}

