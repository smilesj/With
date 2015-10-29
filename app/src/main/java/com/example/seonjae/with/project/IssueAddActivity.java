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
import android.widget.Toast;

import com.example.seonjae.with.R;
import com.example.seonjae.with.StartActivity;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class IssueAddActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token = "";

    private EditText issueTitle;
    private EditText issueContents;
    private EditText issueSolution;
    private Date issueDate;

    private String t_issueTitle;
    private String t_issueContents;
    private String t_issueSolution;
    private String t_issueDate;
    private String t_writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_add);
        registBroadcastReceiver();

        issueTitle = (EditText)findViewById(R.id.issueTitle);
        issueContents = (EditText)findViewById(R.id.issueContents);
        issueSolution = (EditText)findViewById(R.id.issueSolution);

        Button btnAddIssue = (Button)findViewById(R.id.btnAddIssue);
        btnAddIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t_issueTitle = issueTitle.getText().toString();
                t_issueContents = issueContents.getText().toString();
                t_issueSolution = issueSolution.getText().toString();
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                t_issueDate = sdf.format(date);
                t_writer = StartActivity.user_email;
                addIssue();
            }
        });

        Button btnCancleIssue = (Button)findViewById(R.id.btnCancleIssue);
        btnCancleIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addIssue(){
        class addIssueAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("projectID", ProjectHomeActivity.itemProjectID.toString()));
                nameValuePairs.add(new BasicNameValuePair("issueTitle", t_issueTitle));
                nameValuePairs.add(new BasicNameValuePair("issueContents", t_issueContents));
                nameValuePairs.add(new BasicNameValuePair("issueSolution", t_issueSolution));
                nameValuePairs.add(new BasicNameValuePair("issueDate", t_issueDate));
                nameValuePairs.add(new BasicNameValuePair("writer", t_writer));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/issueAdd.php");
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
                    Toast.makeText(getApplicationContext(), "이슈가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    getInstanceIdToken();
                    sendGCMIssue();
                    finish();
                }
                else if(s.equalsIgnoreCase("failure")){
                    Toast.makeText(getApplicationContext(), "이슈가 추가되지않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }

        }
        addIssueAsync la = new addIssueAsync();
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

    private void sendGCMIssue() {
        class sendGCMIssueAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                InputStream is = null;
                String msg = "\""+t_issueTitle+"\"가추가되었습니다.";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("regID", token));
                nameValuePairs.add(new BasicNameValuePair("title", ProjectHomeActivity.itemProjectName));
                nameValuePairs.add(new BasicNameValuePair("type", "Issue"));
                nameValuePairs.add(new BasicNameValuePair("message", msg));
                Iterator<String> iterator = ProjectHomeActivity.itemProjectWorker.keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    String value = ProjectHomeActivity.itemProjectWorker.get(key);
                    if(!value.equals(StartActivity.user_email))
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
                Log.d("--------------SJ 5 :" , s);
            }

        }
        sendGCMIssueAsync la = new sendGCMIssueAsync();
        la.execute();
    }
}
