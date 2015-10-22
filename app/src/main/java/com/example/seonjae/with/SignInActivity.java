package com.example.seonjae.with;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.net.URL;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText passwd;
    private EditText passwd2;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.enableDefaults();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = (EditText)findViewById(R.id.email);
        passwd = (EditText)findViewById(R.id.passwd);
        passwd2 = (EditText)findViewById(R.id.passwd2);
        name = (EditText)findViewById(R.id.name);

        //뒤로가기 버튼 보이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button btnSignIn= (Button)findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean inputOk = true;
                String t_email = email.getText().toString();
                String t_pass = passwd.getText().toString();
                String t_pass2 = passwd2.getText().toString();
                String t_name = name.getText().toString();

                if (t_email.equals("")) {
                    Toast.makeText(SignInActivity.this, "이메일을 입력하세요.", Toast.LENGTH_SHORT).show();
                    inputOk = false;
                    return;
                }
                if (t_pass.equals("")) {
                    Toast.makeText(SignInActivity.this, "비밀번호을 입력하세요.", Toast.LENGTH_SHORT).show();
                    inputOk = false;
                    return;
                }
                if (t_pass2.equals("")) {
                    Toast.makeText(SignInActivity.this, "비밀번호 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                    inputOk = false;
                    return;
                }
                if (t_name.equals("")) {
                    Toast.makeText(SignInActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    inputOk = false;
                    return;
                }
                if(!t_pass.equals(t_pass2)){
                    Toast.makeText(SignInActivity.this, "비밀번호가 일치하지 않습니다 확인하십시오.", Toast.LENGTH_SHORT).show();
                    inputOk = false;
                    return;
                }

                if(inputOk) {
                    try {
                        URL url = new URL("http://with7.cloudapp.net/signin.php?email=" + t_email + "&passwd=" + t_pass + "&name=" + t_name);
                        url.openStream();
                        Toast.makeText(SignInActivity.this, "가입되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, StartActivity.class);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        Button btnCancle = (Button) findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);

                builder.setMessage("가입을 중단 하시겠습니까?").setCancelable(false).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


    // 뒤로가기 버튼 이벤트처리
    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

}
