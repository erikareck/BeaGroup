package com.example.sharon.beagroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class account extends AppCompatActivity {

    //Erika 2018.10.15試圖關閉不使用之Activity
    public static account instance = null;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^"            //String 開頭
                    + "(?=.*[0-9])"       //至少一數字
                    + "(?=.*[a-zA-Z])"   //至少一字母
                    + "(?=\\S+$)"         //不可有空白
                    + ".{5,12}"          //長度介於5~12
                    + "$");              //String結尾

    //EditText name, email, password, password_con;
    ListView listView;
    //ImageView imageView;
    CircleImageView circleImageView;
    TextView textView;
    Button logout;
    String gender = "";

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        listView = (ListView)findViewById(R.id.account);
        //imageView = (ImageView)findViewById(R.id.sex_pic);
        circleImageView = (CircleImageView)findViewById(R.id.searchImage);
        textView = (TextView)findViewById(R.id.account_name);
        logout = (Button)findViewById(R.id.logout_btn);
        getJSON("http://140.113.73.42/account.php");

        //Erika 2018.10.15關閉activity
        instance = this;

    }

    /*public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_logout, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SaveSharedPreference.clear(account.this);
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
                return true;
            }
        });
    }*/

    public void logout(View view){

        SaveSharedPreference.clear(account.this);
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);

        //Erika 2018.10.15試圖關閉不使用之Activity
        account.instance.finish();

    }



    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LayoutInflater inflater = LayoutInflater.from(account.this);
            final View v = LayoutInflater.from(account.this).inflate(R.layout.alertdialog_use, null);
            final View v2 = LayoutInflater.from(account.this).inflate(R.layout.alertdialog_pwd, null);
            switch (position) {
                case 0:
                    new AlertDialog.Builder(account.this).setTitle("通知")
                            .setMessage("ID無法更改")
                            .setPositiveButton("確定", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).show();
                    break;
                case 1:
                    new AlertDialog.Builder(account.this).setTitle("更改名字")
                            .setView(v)
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText name = (EditText) v.findViewById(R.id.alterD);
                            String ch_name = name.getText().toString();
                            String result="";
                            if(ch_name.trim().isEmpty()) {
                                Toast.makeText(getApplicationContext(), "不可為空白", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try {
                                    result = new changeInfo(account.this).execute("name", ch_name).get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                            finish();
                            startActivity(getIntent());
                            if(result.equals("1")){
                                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),  "修改失敗", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    break;
                case 2:
                    new AlertDialog.Builder(account.this).setTitle("更改email")
                            .setView(v)
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText editText = (EditText) v.findViewById(R.id.alterD);
                                    String ch_mail= editText.getText().toString();
                                    String result="";
                                    if(ch_mail.trim().isEmpty()){
                                        Toast.makeText(getApplicationContext(), "不可為空白", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(!Patterns.EMAIL_ADDRESS.matcher(ch_mail).matches()){
                                        Toast.makeText(getApplicationContext(), "請輸入有效email", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        try {
                                            result = new changeInfo(account.this).execute("email", ch_mail).get();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    finish();
                                    startActivity(getIntent());
                                    if(result.equals("1")){
                                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),  "修改失敗", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    break;
                case 3:
                    new AlertDialog.Builder(account.this).setTitle("更改密碼")
                            .setView(v2)
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText editText = (EditText) v2.findViewById(R.id.alterD_pwd);
                                    EditText editText2 = (EditText) v2.findViewById(R.id.alterD_pwdcon);
                                    String ch_pwd= editText.getText().toString();
                                    String ch_pwdcon = editText2.getText().toString();
                                    String result="";
                                    if(ch_pwd.trim().isEmpty()||ch_pwdcon.trim().isEmpty()){
                                        Toast.makeText(getApplicationContext(), "不可為空白", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(!PASSWORD_PATTERN.matcher(ch_pwd).matches()){
                                        Toast.makeText(getApplicationContext(), "至少須一字母、一數字、長度介於5~12", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(!ch_pwd.equals(ch_pwdcon)){
                                        Toast.makeText(getApplicationContext(), "密碼請輸入一致", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        try {
                                            result = new changeInfo(account.this).execute("password", ch_pwd).get();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    finish();
                                    startActivity(getIntent());
                                    if(result.equals("1")){
                                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),  "修改失敗", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    break;

                default:
                    Toast.makeText(account.this,"沒有", Toast.LENGTH_SHORT).show();

            }

        }
    };

//從資料庫抓資料
    private void getJSON(final String urlWebService){
        class  GetJSON extends AsyncTask<Void, Void, String>{
            String currentUser = SaveSharedPreference.getID(account.this);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoView(s);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {

                try{
                    URL url = new URL(urlWebService);           //creating a URL
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();           //Opening the URL using HttpURLConnection
                    httpURLConnection.setRequestMethod("POST"); //以POST傳送
                    httpURLConnection.setDoInput(true); //是否向httpURLConnection輸出:true
                    httpURLConnection.setDoOutput(true); //是否從httpURLConnection讀入:true
                    OutputStream outputStream = httpURLConnection.getOutputStream(); //得到subprocess的輸出流
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(currentUser, "UTF-8");
                    StringBuilder sb = new StringBuilder();         //StringBuilder object to read the string from the service
                    bufferedWriter.write(post_data);//將post_data寫入緩衝區
                    bufferedWriter.flush(); //刷新該stream中的緩衝，將缓衝數據寫到目的文件中(login.php)
                    bufferedWriter.close(); //關閉stream
                    outputStream.close(); //關閉此outputStream並釋放與此stream有關的所有系統資源
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));            //use a buffered reader to read the string from service
                    String json;            //A simple string to read values from each line
                    while ((json = bufferedReader.readLine()) != null) {            //reading until we don't find null

                        //appending it to string builder
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();            //creating asynctask object and executing it
        getJSON.execute();
    }

//將JSON的值存入array
    private void loadIntoView(String json) throws JSONException{
        String  Item[] = {"ID", "使用者名稱", "E-Mail","修改密碼"};
        JSONArray jsonArray = new JSONArray(json);
        String[] AccountInfo = new String[4];



        for(int i =0; i<4; i++){
            JSONObject obj = jsonArray.getJSONObject(0);
            if(i==0)
                AccountInfo[i] = obj.getString("user_id");
            else if(i==1)
                AccountInfo[i] = obj.getString("name");
            else if(i==2)
                AccountInfo[i] = obj.getString("email");
            else if(i==3)
                gender = obj.getString("sex");
            else if(i==4)
                AccountInfo[i] = "";
        }
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Item, AccountInfo);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(onClickListView);
        textView.setText(AccountInfo[1]);
        if(gender.equals("F"))
            circleImageView.setImageResource(R.drawable.girl);
        else if(gender.equals("M"))
            circleImageView.setImageResource(R.drawable.boy);
    }




}
