package com.example.sharon.beagroup;

import android.os.AsyncTask;

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

public class assignGroupID extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params ) { //How: assignGroupID.execute(user_id, group_id);
        String id = params[0];
        String group_id = params[1];
        String check_code="";
        String result="";
        try{
            String uploadLocation_url = "http://140.113.73.42/assignGroupID.php";
            URL url = new URL(uploadLocation_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")  //資料POST的格式
                    +"&"+URLEncoder.encode("group_id", "UTF-8") + "=" + URLEncoder.encode(group_id, "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));


            while ((check_code = bufferedReader.readLine()) != null) {
                if (check_code.equals("1")) //Update成功
                    result = "1";
                else  //Update失敗
                    result = "0";
            }
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
