package com.example.sharon.beagroup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity{
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^"            //String 開頭
                    + "(?=.*[0-9])"       //至少一數字
                    + "(?=.*[a-zA-Z])"   //至少一字母
                    + "(?=\\S+$)"         //不可有空白
                    + ".{5,12}"          //長度介於5~12
                    + "$");              //String結尾
    private static final Pattern ID_PATTERN =
            Pattern.compile("^"            //String 開頭
                    + "(?=.*[a-zA-Z])"   //至少一字母
                    + "(?=\\S+$)"         //不可有空白
                    + ".{6,15}"          //長度介於6~15
                    + "$");              //String結尾
    EditText name, password, password_con, email, id;
    Switch sex;
    TextInputLayout namelayout, passwordconlayout, passwordlayout, emaillayout, idlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.et_name);
        password = (EditText) findViewById(R.id.et_password);
        password_con = (EditText) findViewById(R.id.et_password_con);
        email = (EditText) findViewById(R.id.et_email);
        id = (EditText) findViewById(R.id.et_id);
        sex = (Switch) findViewById(R.id.et_sex);
        namelayout = (TextInputLayout)findViewById(R.id.et_name_layout);
        passwordlayout = (TextInputLayout)findViewById(R.id.et_password_layout); //textinputlayout使edittext能顯示錯誤訊息等
        passwordconlayout = (TextInputLayout)findViewById(R.id.et_password_con_layout);
        emaillayout = (TextInputLayout)findViewById(R.id.et_email_layout);
        idlayout = (TextInputLayout)findViewById(R.id.et_id_layout);
    }

    private boolean validateUsername(){
        if(name.getText().toString().isEmpty()){
            namelayout.setError("不可為空白");
            return false;
        }
        else{
            namelayout.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        if(password.getText().toString().isEmpty()){
            passwordlayout.setError("不可為空白");
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(password.getText().toString()).matches()){
            passwordlayout.setError("至少須一字母、一數字、長度介於5~12");
            return false;
        }
        else{
            passwordlayout.setError(null);
            return true;
        }
    }

    private boolean validatePasswordCon(){
        if(password_con.getText().toString().isEmpty()){
            passwordconlayout.setError("須確認密碼");
            return  false;
        }
        else if(!(password_con.getText().toString().equals(password.getText().toString()))){
            passwordconlayout.setError("輸入密碼不一致");
            return false;
        }
        else {
            passwordconlayout.setError(null);
            return true;
        }
    }

    private boolean validateEmail(){
        if(email.getText().toString().isEmpty()){
            emaillayout.setError("不可為空白");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            emaillayout.setError("請輸入有效email");
            return false;

        }
        else{
            emaillayout.setError(null);
            return true;
        }
    }

    private boolean validateID() throws ExecutionException, InterruptedException {
        String testID =new checkID().execute(id.getText().toString()).get();
        if(id.getText().toString().isEmpty()){
            idlayout.setError("不可為空白");
            return false;
        }
        else if(!ID_PATTERN.matcher(id.getText().toString()).matches()){
            idlayout.setError("至少須一字母、長度介於6~15");
            return false;

        }
        else if(testID.equals("0")){
            idlayout.setError("此ID已有人使用");
            return false;
        }
        else{
            idlayout.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) throws ExecutionException, InterruptedException {
        if (!validateUsername() | !validatePassword() | !validatePasswordCon() | !validateEmail() | !validateID()){
            return;
        }
        else{
            onSignup(getWindow().getDecorView());
        }
    }



    public void onSignup(View view) {
        String str_name = name.getText().toString();
        String str_password = password.getText().toString();
        String str_password_con = password_con.getText().toString();

        String str_email = email.getText().toString();
        String str_id = id.getText().toString();
        String str_sex;
        Boolean switchState = sex.isChecked();
        if (switchState) str_sex = "F"; //設定性別值
        else str_sex = "M";

        String type = "signup";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type, str_name, str_password, str_email, str_id, str_sex); //傳參數(型態：註冊、註冊內容)
        if (backgroundWork.login_code.equals("1")) { //若註冊成功，跳轉至主畫面
            Intent intent = new Intent();
            intent.setClass(signup.this, MainActivity.class);
            startActivity(intent);
        }

    }
}


