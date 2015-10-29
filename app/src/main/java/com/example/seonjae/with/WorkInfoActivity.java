package com.example.seonjae.with;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seonjae.with.data.TodoData;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.hoang8f.widget.FButton;

public class WorkInfoActivity extends AppCompatActivity {

    private String workID;
    private String projectName;
    private String workName = "";
    private String workContents = "";
    private int priority;
    private String endDay= "";

    private LinearLayout layoutWorkInfo;
    private LinearLayout layoutRewriteWorkInfo;
    private TextView valueProjectName;
    private TextView valueWorkName;
    private TextView valueWorkDes;
    private TextView valueWorkPriority;
    private TextView valueStartDay;
    private TextView valueEndDay;
    private FButton btnEndWork;

    private TextView reProjectName;
    private EditText reWorkName;
    private EditText reWorkDes;
    private DiscreteSeekBar reWorkPriority;
    private TextView reStartDay;
    private TextView reEndDay;
    private Button btnEndDay;
    private Button btnReWrite;
    private Button btnCancle;
    static final int DATE_DIALOG_ID = 0;
    private int mYear;
    private int mMonth;
    private int mDay;

    private int complete;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_info);

        Intent intent = getIntent();
        workID = intent.getExtras().getString("workID");
        projectName = intent.getExtras().getString("projectName");
        complete = intent.getExtras().getInt("complete");

        layoutWorkInfo = (LinearLayout)findViewById(R.id.layoutWorkInfo);
        layoutRewriteWorkInfo = (LinearLayout)findViewById(R.id.layoutRewriteWorkInfo);

        valueProjectName = (TextView)findViewById(R.id.valueProjectName);
        valueWorkName = (TextView)findViewById(R.id.valueWorkName);
        valueWorkDes = (TextView)findViewById(R.id.valueWorkDes);
        valueWorkPriority = (TextView)findViewById(R.id.valueWorkPriority);
        valueStartDay = (TextView)findViewById(R.id.valueStartDay);
        valueEndDay = (TextView)findViewById(R.id.valueEndDay);
        btnEndWork = (FButton)findViewById(R.id.btnEndWork);
        if(complete == 1){
            btnEndWork.setButtonColor(Color.rgb(207,207,207));
            btnEndWork.setShadowColor(Color.rgb(153,153,153));
            btnEndWork.setEnabled(false);
        }

        btnEndWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url2 = new URL("http://with7.cloudapp.net/updateWorkComplete.php?email=" + StartActivity.user_email + "&workID=" + workID);
                    url2.openStream();
                    Toast.makeText(getBaseContext(), "업무를 완료하였습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(WorkInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        reProjectName = (TextView)findViewById(R.id.reProjectName);
        reWorkName = (EditText)findViewById(R.id.reWorkName);
        reWorkDes = (EditText)findViewById(R.id.reWorkDes);
        reWorkPriority = (DiscreteSeekBar)findViewById(R.id.rePriority);
        reStartDay = (TextView)findViewById(R.id.reStartDay);
        reEndDay = (TextView)findViewById(R.id.reEndDay);
        btnEndDay = (Button)findViewById(R.id.btnEndDay);
        btnEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] split = reEndDay.getText().toString().split("-");
                mYear = Integer.valueOf(split[0]);
                mMonth = Integer.valueOf(split[1]);
                mDay = Integer.valueOf(split[2]);
                updateDisplay();
                showDialog(DATE_DIALOG_ID);
            }
        });

        btnReWrite = (Button)findViewById(R.id.btnRewrite);
        btnReWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workName = reWorkName.getText().toString();
                workContents = reWorkDes.getText().toString();
                priority = reWorkPriority.getProgress();
                endDay = reEndDay.getText().toString();
                rewriteWorkInfo();
            }
        });

        btnCancle = (Button)findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getWorkInfo();
    }

    private void getWorkInfo(){
        class GetWorkInfoAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("workID", workID));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getWorkInfo.php");
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
                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        valueProjectName.setText(projectName.toString());
                        valueWorkName.setText(jsonObject.getString("workName"));
                        valueWorkDes.setText(jsonObject.getString("workContents"));
                        valueWorkPriority.setText(jsonObject.getString("priority"));
                        valueStartDay.setText(jsonObject.getString("startDay"));
                        valueEndDay.setText(jsonObject.getString("endDay"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        GetWorkInfoAsync la = new GetWorkInfoAsync();
        la.execute();
    }


    private void rewriteWorkInfo(){
        class RewriteWorkInfoAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("workID", workID));
                nameValuePairs.add(new BasicNameValuePair("workName", workName));
                nameValuePairs.add(new BasicNameValuePair("workContents", workContents));
                nameValuePairs.add(new BasicNameValuePair("priority", String.valueOf(priority)));
                nameValuePairs.add(new BasicNameValuePair("endDay", endDay));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/rewriteWorkInfo.php");
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
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(WorkInfoActivity.this, "업무가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WorkInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        }
        RewriteWorkInfoAsync la = new RewriteWorkInfoAsync();
        la.execute();
    }

    private void deleteWorkInfo(){
        class DeleteWorkInfoAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("workID", workID));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/deleteWorkInfo.php");
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
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(WorkInfoActivity.this, "업무가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WorkInfoActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        }
        DeleteWorkInfoAsync la = new DeleteWorkInfoAsync();
        la.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.workinfo_rewrite) {
            layoutWorkInfo.setVisibility(LinearLayout.INVISIBLE);
            layoutRewriteWorkInfo.setVisibility(LinearLayout.VISIBLE);
            reProjectName.setText(projectName.toString());
            reWorkName.setText(valueWorkName.getText());
            reWorkDes.setText(valueWorkDes.getText());
            reWorkPriority.setProgress(Integer.valueOf(valueWorkPriority.getText().toString()));
            reStartDay.setText(valueStartDay.getText());
            reEndDay.setText(valueEndDay.getText());
            return true;
        }
        else if (id == R.id.workinfo_delete) {
            deleteWorkInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateDisplay() {
        reEndDay.setText(new StringBuilder()
                .append(mYear).append("-")
                .append(mMonth + 1).append("-")
                .append(mDay));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =  new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch(id)
        {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }
}
