package com.example.sharon.beagroup;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class signup extends AppCompatActivity {

    EditText name, password, password_con, email, id;
    Switch sex;
    TextInputLayout passwordconlayout, passwordlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //鍵盤不自動彈出
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = (EditText) findViewById(R.id.et_name);
        password = (EditText) findViewById(R.id.et_password);
        password_con = (EditText) findViewById(R.id.et_password_con);
        email = (EditText) findViewById(R.id.et_email);
        id = (EditText) findViewById(R.id.et_id);
        sex = (Switch) findViewById(R.id.et_sex);
        passwordlayout = (TextInputLayout)findViewById(R.id.et_password_layout);
        passwordconlayout = (TextInputLayout)findViewById(R.id.et_password_con_layout);

        password_con.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(password.getText().toString()))
                    passwordconlayout.setError(null);
                else
                    passwordconlayout.setError("輸入密碼不一致");
            }

        });
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
        backgroundWork.execute(type, str_name, str_password, str_email, str_id, str_sex);
        if (backgroundWork.login_code.equals("1")) {
            Intent intent = new Intent();
            intent.setClass(signup.this, MainActivity.class);
            startActivity(intent);
        }

    }

}


