package com.example.seonjae.with.dummy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seonjae.with.R;
import com.example.seonjae.with.StartActivity;
import com.example.seonjae.with.data.RequestData;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.widget.FButton;

/**
 * Created by seonjae on 2015-10-29.
 */
@SuppressLint("ViewHolder")
public class ListRequestAdapter extends BaseAdapter {

    private Context context;
    private RequestData rData;
    private ArrayList<RequestData> RequestList;

    private TextView projectName;
    private TextView workName;
    private TextView requestDate;
    private RadioGroup radioG;
    private RadioButton btnYes;
    private RadioButton btnNo;
    private FButton btnSend;
    private String currentWorkID;
    private String currentProjectName;
    private int select = 0;
    public ListRequestAdapter(Context context, ArrayList<RequestData> RequestList){
        super();
        this.context = context;
        this.RequestList = RequestList;
    }

    @Override
    public int getCount() {
        return RequestList.size();
    }

    @Override
    public RequestData getItem(int position) {
        return RequestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;

        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.requestlist_item, parent, false);
        }
        projectName = (TextView)convertView.findViewById(R.id.projectName);
        workName = (TextView)convertView.findViewById(R.id.workName);
        requestDate = (TextView)convertView.findViewById(R.id.requestDate);
        radioG = (RadioGroup)convertView.findViewById(R.id.radioG);
        radioG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == btnYes.getId())
                    select = 1;
                else if(checkedId == btnNo.getId())
                    select = 2;

                Log.d("--SJ00: ", "  - " + select);
            }
        });
        btnYes = (RadioButton)convertView.findViewById(R.id.btnYes);
        btnNo = (RadioButton)convertView.findViewById(R.id.btnNo);
        btnSend = (FButton)convertView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWorkID = RequestList.get(position).getWorkID();
                currentProjectName = RequestList.get(position).getProjectName();
                requestFunc();
                Toast.makeText(context.getApplicationContext(), "확인하였습니다.", Toast.LENGTH_LONG).show();
            }
        });

        rData = getItem(position);

        if(rData != null){
            projectName.setText(RequestList.get(position).getProjectName());

            projectName.setText(RequestList.get(position).getProjectName());
            workName.setText(RequestList.get(position).getWorkName());
            requestDate.setText(String.valueOf(RequestList.get(position).getRequestDate()));
        }
        return convertView;
    }

    //<work>, <workCharge> ADD, <request> Delete
    private void requestFunc(){

        class RequestWorkAddAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("workID", currentWorkID));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/addRequestWork.php");
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
                String t_resiEmail = StartActivity.user_email;
                String s = result.trim();

                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        try{
                            URL url = new URL("http://with7.cloudapp.net/workChargeAdd.php?projectID=" + jsonObject.getString("projectID")
                                    +"&workerEmail=" + t_resiEmail + "&workID=" + jsonObject.getString("workID") + "&workName=" + jsonObject.getString("workName")
                                    + "&projectName=" + currentProjectName + "&endDay="+jsonObject.getString("endDay")+ "&priority=" + jsonObject.getInt("priority"));
                            url.openStream();

                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        class RequestWorkDeleteAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String t_email = StartActivity.user_email;

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("worker", t_email));
                nameValuePairs.add(new BasicNameValuePair("workID", currentWorkID));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/deleteRequestWork.php");
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
                if(select == 1){
                    RequestWorkAddAsync la = new RequestWorkAddAsync();
                    la.execute();
                }
            }

        }
        RequestWorkDeleteAsync ld = new RequestWorkDeleteAsync();
        ld.execute();
    }

    public void addRequest(RequestData data){
        RequestList.add(data);
    }
}
