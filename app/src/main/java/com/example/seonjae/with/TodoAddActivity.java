package com.example.seonjae.with;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seonjae.with.dummy.ListTodoAdapter;
import com.example.seonjae.with.dummy.MP_Project_Fragment;
import com.example.seonjae.with.dummy.MP_TODO_Fragment;
import com.example.seonjae.with.gcm.RegistrationIntentService;
import com.example.seonjae.with.project.ProjectHomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import info.hoang8f.widget.FButton;

public class TodoAddActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String token = "";

    private TextView endDate;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Spinner spinner;
    private EditText workName;
    private EditText workDescription;
    private DiscreteSeekBar priority;
    private DataConn dataConn;
    private Map<String,String> projectInfo;

    private String pID;
    private String pName;
    private ArrayAdapter<String> workerAdapter;
    private ArrayList<String> workerList;
    private LinearLayout workerLayout;
    private CheckBox[] checkBox;
    private Map<String, String> gcmWorkerList;
    static final int DATE_DIALOG_ID = 0;
    private String t_workName;
    final CountDownLatch signal = new CountDownLatch(1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);
        registBroadcastReceiver();

        gcmWorkerList = new HashMap<String, String>();
        dataConn = new DataConn();
        ArrayList<String> projectNameList = new ArrayList<String>();
        projectNameList.addAll(dataConn.getProjectNameList());

        projectInfo = new HashMap<String, String>();
        projectInfo.putAll(dataConn.getProjectInfo());

        ArrayAdapter<String> adapter = new  ArrayAdapter<String> (this , android.R.layout.simple_spinner_item, projectNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setSelection(0);
        spinner.setAdapter(adapter);
        pName = spinner.getSelectedItem().toString();
        Iterator<String> it = projectInfo.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            String value = projectInfo.get(key);

            if(value.equals(pName)){
                pID = key;
            }
        }

        workerLayout = (LinearLayout)findViewById(R.id.workerLayout);
        workerList = new ArrayList<String>();
        Iterator<String> iterator = MP_Project_Fragment.team.get(pID).keySet().iterator();
        checkBox = new CheckBox[MP_Project_Fragment.team.size()];
        int i = 0;
        while(iterator.hasNext()){
            String key = iterator.next();
            i++;
            checkBox[i] = new CheckBox(this);
            checkBox[i].setTextColor(Color.DKGRAY);
            checkBox[i].setText(key);
            workerLayout.addView(checkBox[i]);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                pName = spinner.getSelectedItem().toString();

                Iterator<String> iterator = projectInfo.keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    String value = projectInfo.get(key);

                    if(value.equals(pName)){
                        pID = key;
                    }
                }

                workerList.clear();
                workerLayout.removeAllViewsInLayout();
                checkBox = new CheckBox[MP_Project_Fragment.team.size()];
                int i = 0;
                Iterator<String> iterator2 = MP_Project_Fragment.team.get(pID).keySet().iterator();
                while(iterator2.hasNext()) {
                    String key = iterator2.next();
                    i++;
                    checkBox[i] = new CheckBox(getBaseContext());
                    checkBox[i].setTextColor(Color.DKGRAY);
                    checkBox[i].setText(key);
                    workerLayout.addView(checkBox[i]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        workName = (EditText) findViewById(R.id.workName);
        workDescription = (EditText)findViewById(R.id.workDescription);
        endDate = (TextView)findViewById(R.id.endDate);
        priority = (DiscreteSeekBar)findViewById(R.id.sbBar);

        Button selectEnddate = (Button)findViewById(R.id.selectEnddate);
        selectEnddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDisplay();

        FButton btnAddWork = (FButton)findViewById(R.id.btnAddWork);
        btnAddWork.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String t_workID;
                  String t_projectID = "";
                  Iterator<String> iterator = projectInfo.keySet().iterator();
                  while(iterator.hasNext()){
                      String key = iterator.next();
                      String value = projectInfo.get(key);
                      if(value.equals(spinner.getSelectedItem().toString())){
                          t_projectID = key;
                          break;
                      }
                  }
                  t_workName = workName.getText().toString();
                  String t_workDescription = workDescription.getText().toString();
                  int t_priority = priority.getProgress();
                  Date date = new Date();
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                  String t_startDate = sdf.format(date);
                  String t_endDate = endDate.getText().toString();
                  String t_resiEmail = StartActivity.user_email; //수정하기

                  char ch1 = (char) ((Math.random() * 26) + 65);
                  char ch2 = (char) ((Math.random() * 26) + 65);
                  char ch3 = (char) ((Math.random() * 26) + 65);
                  char ch4 = (char) ((Math.random() * 26) + 65);
                  t_workID = String.valueOf(ch1) + String.valueOf(ch2) + String.valueOf(ch3) + String.valueOf(ch4) + t_startDate.substring(0, 8);

                  try{
                      URL url = new URL("http://with7.cloudapp.net/workAdd.php?workID=" + t_workID +"&projectID="+t_projectID
                              + "&workName=" + t_workName + "&workContents=" + t_workDescription + "&complete=0"
                              + "&priority=" + t_priority + "&startDay=" + t_startDate + "&endDay="+t_endDate+ "&resiEmail=" +t_resiEmail);
                      url.openStream();

                  }catch(IOException e){
                      e.printStackTrace();
                  }

//                  try {
//                      URL url2 = new URL("http://with7.cloudapp.net/workChargeAdd.php?projectID=" + t_projectID
//                              +"&workerEmail=" + t_resiEmail + "&workID=" + t_workID + "&workName=" + t_workName
//                              + "&projectName=" +projectInfo.get(t_projectID) + "&endDay="+t_endDate+ "&priority=" + t_priority);
//                      url2.openStream();
//
//                  } catch (IOException e){
//                      e.printStackTrace();
//                  }

                  try {
                      workerIsChecked();
                      for(int i = 0; i < workerList.size(); i++) {
                          URL url3 = new URL("http://with7.cloudapp.net/workRequestAdd.php?workID=" + t_workID
                                  + "&worker=" + workerList.get(i) + "&projectID=" + t_projectID + "&workName=" + t_workName
                                  + "&projectName=" + projectInfo.get(t_projectID) + "&date=" + t_startDate + "&writer=" + t_resiEmail);
                          url3.openStream();
                      }
                      Toast.makeText(TodoAddActivity.this, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                      getInstanceIdToken();
                      sendGCMTodo();
                      finish();
                  } catch (IOException e){
                      e.printStackTrace();
                  }
              }
          }
        );
    }

    private void workerIsChecked(){
        workerList.clear();
        for(int j=0; j < workerLayout.getChildCount(); j++){
            View v = workerLayout.getChildAt(j);
            if(v instanceof CheckBox){
                if (((CheckBox) v).isChecked()) {
                    workerList.add(((CheckBox) v).getText().toString());
                }
            }
        }
    }

    private void updateDisplay() {
        endDate.setText(new StringBuilder()
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

    private void sendGCMTodo() {

        class sendGCMTodoAsync extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {

                super.onPreExecute();

            }
            @Override
            protected String doInBackground(String... params) {
                try {
                    signal.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("--SJ9","6");
                InputStream is = null;
                String msg = "\""+t_workName+"\"가요청되었습니다.";
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("regID", token));
                nameValuePairs.add(new BasicNameValuePair("title", ProjectHomeActivity.itemProjectName));
                nameValuePairs.add(new BasicNameValuePair("type", "RequestWork"));
                nameValuePairs.add(new BasicNameValuePair("message", msg));
                Iterator<String> iterator = gcmWorkerList.keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    String value = gcmWorkerList.get(key);
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

        class GetWorkerRegIDAsync extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                workerIsChecked();
            }
            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                Log.d("---SJ_workerList:", "" + workerList.toString());
                nameValuePairs.add(new BasicNameValuePair("cnt", String.valueOf(workerList.size())));
                for(String str : workerList){
                    nameValuePairs.add(new BasicNameValuePair("workers[]", str));
                }
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getWorkerRegID.php");
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
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        gcmWorkerList.put(jsonObject.getString("workerEmail"), jsonObject.getString("workerRegID"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("---SJ_gcmWorkerList:", ""+gcmWorkerList.toString());
                signal.countDown();
                sendGCMTodoAsync la = new sendGCMTodoAsync();
                la.execute();
            }

        }

        GetWorkerRegIDAsync lw = new GetWorkerRegIDAsync();
        lw.execute();

    }
}

