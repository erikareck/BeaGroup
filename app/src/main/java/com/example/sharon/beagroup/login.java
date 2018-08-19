package com.example.sharon.beagroup;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static android.support.v4.content.ContextCompat.startActivity;

public class login extends AppCompatActivity {

    EditText edUsername, edUserpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get Reference to variables
        edUsername = (EditText) findViewById(R.id.usrname);
        edUserpwd = (EditText) findViewById(R.id.pwd);

    }

    public void onLogin(View view){

        String username = edUsername.getText().toString();
        String password = edUserpwd.getText().toString();
        String type = "login";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type, username,password); //傳參數(型態：登入、登入內容)
       if(backgroundWork.login_code.equals("1")){ //若登入成功，跳轉至主畫面
            Intent intent = new Intent();
            intent.setClass(login.this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void openSignup(View view){
        startActivity(new Intent(this, signup.class));
    } //按下Signup進入註冊畫面



    public interface AsyncResponse {
        void processFinish(String output);
    }






}

