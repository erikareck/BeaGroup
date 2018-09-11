package com.example.sharon.beagroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class account extends AppCompatActivity {

    ListView listView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        listView = (ListView)findViewById(R.id.account);
        imageView = (ImageView)findViewById(R.id.sex_pic);
        getJSON("http://140.113.73.42/account.php");


        //CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Item, AccountInfo);
        //listView.setAdapter(customAdapter);

    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LayoutInflater inflater = LayoutInflater.from(account.this);
            final View v = inflater.inflate(R.layout.alertdialog_use, null);
            switch (position) {
                case 0:
                    new AlertDialog.Builder(account.this).setTitle("通知").setMessage("ID無法更改").show();
                    break;
                case 1:
                    new AlertDialog.Builder(account.this).setTitle("更改名字").setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = (EditText) (v.findViewById(R.id.change_field));
                            Toast.makeText(getApplicationContext(), "你的名字是" +

                                    editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                    break;
                case 2:
                    new AlertDialog.Builder(account.this).setTitle("測試").setMessage("更改MAIL").show();
                    break;
                default:
                    Toast.makeText(account.this,"沒有", Toast.LENGTH_SHORT).show();

            }

        }
    };

    private void getJSON(final String urlWebService){
        class  GetJSON extends AsyncTask<Void, Void, String>{
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(account.this);
            String currentUser = preferences.getString("USER", "null");

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
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

    private void loadIntoView(String json) throws JSONException{
        String  Item[] = {"ID", "使用者名稱", "E-Mail"};
        JSONArray jsonArray = new JSONArray(json);
        String[] AccountInfo = new String[3];


        for(int i =0; i<3; i++){
            JSONObject obj = jsonArray.getJSONObject(0);
            if(i==0)
                AccountInfo[i] = obj.getString("user_id");
            else if(i==1)
                AccountInfo[i] = obj.getString("name");
            else if(i==2)
                AccountInfo[i] = obj.getString("email");
        }
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Item, AccountInfo);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(onClickListView);
    }


}
