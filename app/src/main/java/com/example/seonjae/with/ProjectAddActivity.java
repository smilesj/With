package com.example.seonjae.with;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seonjae.with.data.NoticeData;
import com.example.seonjae.with.dummy.ListSearchAdapter;
import com.example.seonjae.with.dummy.MP_TODO_Fragment;

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
import java.util.List;

import info.hoang8f.widget.FButton;

public class ProjectAddActivity extends AppCompatActivity {

    private EditText projectName;
    private EditText projectDescribe;
    private TextView resultText;

    private EditText searchEmail;
    private FButton btnSearch;
    private String t_email;
    private String t_projectName;
    private String t_projectID;

    private ListView searchListView = null;
    private ArrayList<String> searchDataList;
    private ListSearchAdapter searchListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_add);

        searchDataList = new ArrayList<String>();
        searchListView = (ListView)findViewById(R.id.searchList);
        searchListAdapter = new ListSearchAdapter(getBaseContext(), searchDataList);
        searchListView.setAdapter(searchListAdapter);

        //뒤로가기 버튼 보이기
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        projectName = (EditText)findViewById(R.id.projectName);
        projectDescribe = (EditText)findViewById(R.id.projectDescribe);
        searchEmail = (EditText)findViewById(R.id.workerSearch);
        resultText= (TextView)findViewById(R.id.resultText);

        btnSearch = (FButton)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("--SJ1:","Click!");
                t_email = searchEmail.getText().toString();
                searchEmailData();
            }
        });

        FButton btnCProject = (FButton)findViewById(R.id.btnCProject);
        btnCProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t_projectName = projectName.getText().toString();
                String t_projectDescribe = projectDescribe.getText().toString();
                int t_progress = 0;
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                String t_createDate = sdf.format(date);
                String t_readerEmail = StartActivity.user_email;            //수정하기
                String t_color = null;                  //수정하기


                //랜덤문자 해결하기
                char ch1 = (char) ((Math.random() * 26) + 65);
                char ch2 = (char) ((Math.random() * 26) + 65);
                t_projectID = String.valueOf(ch1) + String.valueOf(ch2) + t_createDate.substring(0, 8); //문자2 + 오늘날짜 //AD20151008

                try{
                    URL url = new URL("http://with7.cloudapp.net/projectAdd.php?projectID=" + t_projectID
                            + "&projectName=" + t_projectName + "&projectDescribe=" + t_projectDescribe
                            + "&progress=" + t_progress + "&createDate=" + t_createDate + "&readerEmail=" +t_readerEmail +"&color="+t_color);
                    url.openStream();

                }catch(IOException e){
                    e.printStackTrace();
                }

                try{
                    URL url2 = new URL("http://with7.cloudapp.net/participationAdd.php?projectID=" + t_projectID
                            + "&email="+t_readerEmail + "&projectName=" + t_projectName + "&pRate=0&pPriority=0");
                    url2.openStream();
                }catch (IOException e){
                    e.printStackTrace();
                }

                for(String str : searchDataList){
                    partWorker(str);
                }

                Toast.makeText(ProjectAddActivity.this, "프로젝트가 생성되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProjectAddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void partWorker(String _email){
        try{
            URL url2 = new URL("http://with7.cloudapp.net/participationAdd.php?projectID=" + t_projectID
                    + "&email="+_email + "&projectName=" + t_projectName + "&pRate=0&pPriority=0");
            url2.openStream();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void searchEmailData(){
        class SearchEmailAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", t_email));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/searchEmail.php");
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
                Log.d("--SJ0 :", s);
                if(s.equals("1")){
                    resultText.setText("팀원이 추가되었습니다.");
                    searchEmail.setText("");
                    searchDataList.add(t_email);
                }
                else if(s.equals("0")){
                    searchEmail.setText("");
                    resultText.setText("해당 팀원을 찾을 수 없습니다.");
                }
            }

        }

        SearchEmailAsync lw = new SearchEmailAsync();
        lw.execute();

    }
    // 뒤로가기 버튼 이벤트처리
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(ProjectAddActivity.this, MainActivity.class);
        startActivity(intent);
        return super.onSupportNavigateUp();
    }
}
