package com.example.seonjae.with.project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.seonjae.with.R;
import com.example.seonjae.with.StartActivity;
import com.example.seonjae.with.data.WorkerData;
import com.example.seonjae.with.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class NoticeAddActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token = "";

    private EditText noticeTitle;
    private EditText noticeContents;
    private Date noticeDate;

    private String t_noticeTitle;
    private String t_noticeContents;
    private String t_noticeDate;
    private String t_writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_add);
        registBroadcastReceiver();
        noticeTitle = (EditText)findViewById(R.id.noticeTitle);
        noticeContents = (EditText)findViewById(R.id.noticeContents);

        Button btnAddNotice = (Button)findViewById(R.id.btnAddNotice);
        btnAddNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t_noticeTitle = noticeTitle.getText().toString();
                t_noticeContents = noticeContents.getText().toString();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                t_noticeDate = sdf.format(date);
                t_writer = StartActivity.user_email;
                addNotice();
            }
        });

        Button btnCancleNotice = (Button)findViewById(R.id.btnCancleNotice);
        btnCancleNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addNotice(){
        class addNoticeAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("projectID", ProjectHomeActivity.itemProjectID.toString()));
                nameValuePairs.add(new BasicNameValuePair("noticeTitle", t_noticeTitle));
                nameValuePairs.add(new BasicNameValuePair("noticeContents", t_noticeContents));
                nameValuePairs.add(new BasicNameValuePair("noticeDate", t_noticeDate));
                nameValuePairs.add(new BasicNameValuePair("writer", t_writer));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/noticeAdd.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                Log.d("------------SJ13: ", s);
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(getApplicationContext(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
                    getInstanceIdToken();
                    sendGCMNotice();
                    finish();
                }
                else if(s.equalsIgnoreCase("failure")){
                    Toast.makeText(getApplicationContext(), "추가되지않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }
        addNoticeAsync la = new addNoticeAsync();
        la.execute();
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals("registrationComplete")){
                    token = intent.getStringExtra("token");
                }
            }
        };
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private void sendGCMNotice() {
        class sendGCMNoticeAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                InputStream is = null;
                String msg = "\""+t_noticeTitle+"\"가추가되었습니다.";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("regID", token));
                nameValuePairs.add(new BasicNameValuePair("title", ProjectHomeActivity.itemProjectName));
                nameValuePairs.add(new BasicNameValuePair("type", "Notice"));
                nameValuePairs.add(new BasicNameValuePair("message", msg));
                Iterator<String> iterator = ProjectHomeActivity.itemProjectWorker.keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    String value = ProjectHomeActivity.itemProjectWorker.get(key);
                    if(!key.equals(StartActivity.user_email))
                        nameValuePairs.add(new BasicNameValuePair("devices[]", value));
                }
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/gcmSendMassage.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));

                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
            }

        }
        sendGCMNoticeAsync la = new sendGCMNoticeAsync();
        la.execute();
    }
}
