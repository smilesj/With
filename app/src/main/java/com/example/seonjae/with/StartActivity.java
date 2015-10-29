package com.example.seonjae.with;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seonjae.with.gcm.GcmActivity;
import com.example.seonjae.with.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class StartActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token;

    private EditText loginEmail;
    private EditText loginPasswd;

    String email;
    String password;
    static public String user_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getInstanceIdToken();
        registBroadcastReceiver();

        startActivity(new Intent(this, SplashActivity.class));

        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPasswd = (EditText)findViewById(R.id.loginPasswd);

        FButton btnLogin = (FButton)findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = loginEmail.getText().toString();
                password = loginPasswd.getText().toString();
                loginWith(email, password);
            }
        });

        TextView signUp = (TextView)findViewById(R.id.loginSignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        Button btnGcm = (Button)findViewById(R.id.GcmButton);
        btnGcm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, GcmActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginWith(final String email, String password){
        class LoginAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String t_email = params[0];
                String t_passwd = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", t_email));
                nameValuePairs.add(new BasicNameValuePair("passwd", t_passwd));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/login2.php");
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

                if (s.equalsIgnoreCase("success")) {
//                    Intent intent = new Intent(MainActivity.this, UserProfile.class);
//                    intent.putExtra(USER_NAME, username);
//                    finish();
//                    startActivity(intent);
                    user_email = email;
                    try {
                        URL url2 = new URL("http://with7.cloudapp.net/updateRegID.php?email=" + email + "&regID=" + token);
                        url2.openStream();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    //intent.putExtra("userEmail", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }
        LoginAsync la = new LoginAsync();
        la.execute(email, password);
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
}
