package com.example.sharon.beagroup;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

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
import java.net.URL;
import java.net.URLEncoder;

import static com.estimote.coresdk.common.config.EstimoteSDK.getApplicationContext;

public class checkFriendID extends AsyncTask<String, Void, String> {

    //MyApplication myApplication = (MyApplication) getApplicationContext();

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
    @Override
    protected String doInBackground(String... params ) { //How: checkFriendID.execute(group_id, friend_id);
        String group_id = params[0];
        String friend_id = params[1];
        String check_code="";
        String result="";
        String friendCheck="";
        //MyApplication myApplication = (MyApplication) getApplicationContext();
        try{
            String checkid_url = "http://140.113.73.42/checkFriendID.php";
            URL url = new URL(checkid_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            //String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");  //資料POST的格式

            String post_data = URLEncoder.encode("Group_ID", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8")  //資料POST的格式
                    +"&"+URLEncoder.encode("friend_ID", "UTF-8") + "=" + URLEncoder.encode(friend_id, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            //MyApplication myApplication = (MyApplication) getApplicationContext();
            //myApplication.setFriendCheck("");

            //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SaveSharedPreference.setFriendCheckCode(getApplicationContext(), "");

            while ((check_code = bufferedReader.readLine()) != null) {


                if (check_code.equals("1")) { //already a friend

                    SaveSharedPreference.setFriendCheckCode(getApplicationContext(), "1");
                    Log.d("checkFriendID", friend_id + " is already a friend.");
                    result = "1";
                    return result;
                } else {  //not a friend

                    SaveSharedPreference.setFriendCheckCode(getApplicationContext(), "0");
                    result = "0";
                }
            }
            Log.d("checkFriendID", friend_id + " is not a friend.");
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();


        }
        catch (UnsupportedEncodingException e) {
            result = "0";
        } catch (MalformedURLException e) {
            result = "0";
        } catch (IOException e) {
            result = "0";
        }
        return result;
    }
}
