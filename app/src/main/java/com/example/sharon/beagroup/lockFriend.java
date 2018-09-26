package com.example.sharon.beagroup;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class lockFriend extends AppCompatActivity{

    EditText searchID;
    TextView textView;
    String[] UserInfo = new String[3];
    String result="";
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_friend);

        searchID = (EditText) findViewById(R.id.searchText);
        textView = (TextView)findViewById(R.id.searchName);
        circleImageView = (CircleImageView)findViewById(R.id.searchImage);

    }

    public void openJSON(View view){
        getJSON("http://140.113.73.42/search.php");
    }


    public void getJSON(final String urlWebService){
        class  GetJSON extends AsyncTask<Void, Void, String>{

            //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(account.this);
            //String currentUser = preferences.getString("USER", "null");
            String IDText = searchID.getText().toString();



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {

                    loadIntoView(s);

                }catch (JSONException e){
                    //e.printStackTrace();
                    textView.setText("ID錯誤");
                    //Toast.makeText(getApplicationContext(), "ID錯誤", Toast.LENGTH_SHORT).show();
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
                    String post_data = URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(IDText, "UTF-8");
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
        String gender = "";
        if(!json.equals("")) {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < 2; i++) {

                JSONObject obj = jsonArray.getJSONObject(0);
                if (i == 0) {
                    UserInfo[i] = obj.getString("name");
                } else if (i == 1) {
                    UserInfo[i] = obj.getString("sex");
                    gender = UserInfo[1];
                }
            }
            textView.setText(UserInfo[0]);
            if(gender.equals("F"))
                circleImageView.setImageResource(R.drawable.girl);
            else if(gender.equals("M"))
                circleImageView.setImageResource(R.drawable.boy);
        }



        /*if(!result.equals("error")){
            textView.setText("HIhi"+UserInfo[0]);
        }*/
    }
}


