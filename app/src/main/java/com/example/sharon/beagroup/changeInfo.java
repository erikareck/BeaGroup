package com.example.sharon.beagroup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class changeInfo extends AsyncTask<String, Void, String>{
    Context context;
    changeInfo(Context ctx){
        context = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    @Override
    protected String doInBackground(String... params) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String currentUser = preferences.getString("USER", "null");
        String type = params[0];
        String info = params[1];
        String result = "";
        String check_code="";
        /*switch (type){
            case "1":
                code = "name";
                break;
            case "2":
                code = "email";
                break;
            case "3":
                code = "password";
                break;
        }*/
        try{
            String change_url = "http://140.113.73.42/change.php";
            URL url = new URL(change_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("info", "UTF-8") + "=" + URLEncoder.encode(info, "UTF-8")
                    +"&"+URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")
                    +"&"+URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(currentUser, "UTF-8");  //資料POST的格式
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            while((check_code = bufferedReader.readLine()) != null){ //從緩衝區讀取數據
                /*if(check_code.equals("1")) //資料正確
                    result = "1";
                else if(check_code.equals("0"))  //資料錯誤
                    result = "0";*/
                result +=check_code;


            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();


        } catch (UnsupportedEncodingException e) {
            result = "0";
        } catch (MalformedURLException e) {
            result = "0";
        } catch (IOException e) {
            result = "0";
        }
        return result;
    }
}
