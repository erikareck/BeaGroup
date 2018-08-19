package com.example.sharon.beagroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean logon = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!logon){
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
        }
    }
}
