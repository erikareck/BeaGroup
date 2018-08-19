package com.example.sharon.beagroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

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

import static android.support.v4.content.ContextCompat.startActivity;

public class BackgroundWork extends AsyncTask<String, Void, String>{
    AlertDialog alertDialog;
    Context context;
    String login_code="";
    String signup_code="";
    BackgroundWork(Context ctx){
        context = ctx;
    }

    @Override
    protected String  doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://140.113.73.42/login.php";
        String signup_url = "http://140.113.73.42/signup.php";
        if(type.equals("login")){
            try {
                String user_name = params[1];
                String password = params[2];
                URL url  = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name, "UTF-8")+"&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8"); //資料POST的格式
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";

                while((login_code = bufferedReader.readLine()) != null){
                    if(login_code.equals("1")) //資料正確
                        result+="Login success";
                    else if(login_code.equals("0"))  //資料錯誤
                        result+="Login failed";

                    //result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (type.equals("signup")){

            try {
                String name = params[1];
                String password = params[2];
                String email = params[3];
                String id = params[4];
                String sex = params[5];
                URL url  = new URL(signup_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("usr_id","UTF-8")+"="+URLEncoder.encode(id, "UTF-8")
                        +"&"+URLEncoder.encode("usr_name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")
                        +"&"+URLEncoder.encode("usr_password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")
                        +"&"+URLEncoder.encode("usr_email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")
                        +"&"+URLEncoder.encode("usr_sex", "UTF-8")+"="+URLEncoder.encode(sex, "UTF-8");  //資料POST的格式
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";

                while((signup_code = bufferedReader.readLine()) != null){
                    if(signup_code.equals("1")) //資料正確
                        result+="Signup  success";
                    else  //資料錯誤
                        result+="signup  failed";

                    //result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



        return  null;


    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(aVoid);

        //alertDialog.setMessage(result);
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
       // alertDialog.show();
        if(result.equals("Login success")) {
            ((Activity) context).finish();
        }
        if(result.equals("Signup  success")) {
            ((Activity) context).finish();
        }

    }
}
