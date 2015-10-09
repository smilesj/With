package com.example.seonjae.with;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectAddActivity extends AppCompatActivity {

    private EditText projectName;
    private EditText projectDescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_add);

        //뒤로가기 버튼 보이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        projectName = (EditText)findViewById(R.id.projectName);
        projectDescribe = (EditText)findViewById(R.id.projectDescribe);

        Button btnCProject = (Button)findViewById(R.id.btnCProject);
        btnCProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t_projectID;
                String t_projectName = projectName.getText().toString();
                String t_projectDescribe = projectDescribe.getText().toString();
                int t_progress = 0;
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                String t_createDate = sdf.format(date);
                String t_readerEmail = "test@mail.com";            //수정하기
                String t_color = null;                  //수정하기



                //랜덤문자 해결하기
                char ch1 = (char) ((Math.random() * 26) + 65);
                char ch2 = (char) ((Math.random() * 26) + 65);
                t_projectID = String.valueOf(ch1) + String.valueOf(ch2) + t_createDate.substring(0, 8); //문자2 + 오늘날짜 //AD20151008
                Log.d("------------SJ2: ", t_projectID);
                try{
                    URL url = new URL("http://with7.cloudapp.net/projectAdd.php?projectID=" + t_projectID
                            + "&projectName=" + t_projectName + "&projectDescribe=" + t_projectDescribe
                            + "&progress=" + t_progress + "&createDate=" + t_createDate + "&readerEmail=" +t_readerEmail +"&color="+t_color);
                    url.openStream();

                    Toast.makeText(ProjectAddActivity.this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    e.printStackTrace();
                }

                try{
                    URL url2 = new URL("http://with7.cloudapp.net/participationAdd.php?projectID=" + t_projectID
                            + "&email="+t_readerEmail + "&projectName=" + t_projectName + "&pRate=0&pPriority=0");
                    url2.openStream();
                    Toast.makeText(ProjectAddActivity.this, "추가되었습니다2.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProjectAddActivity.this, MainActivity.class);
                    startActivity(intent);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

    }

    // 뒤로가기 버튼 이벤트처리
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(ProjectAddActivity.this, MainActivity.class);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }
}
