package com.example.seonjae.with;

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
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPasswd;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPasswd = (EditText)findViewById(R.id.loginPasswd);

        Button btnLogin = (Button)findViewById(R.id.loginButton);
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
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
}