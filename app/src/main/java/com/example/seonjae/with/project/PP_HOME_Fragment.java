package com.example.seonjae.with.project;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import android.view.View.OnClickListener;
import android.widget.Toast;


import com.example.seonjae.with.R;
import com.example.seonjae.with.data.IssueData;
import com.example.seonjae.with.data.NoticeData;
import com.example.seonjae.with.dummy.ListIssueAdapter;
import com.example.seonjae.with.dummy.ListNoticeAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PP_HOME_Fragment extends Fragment {

    private View view;
    private ListView noticeListView = null;
    private ListNoticeAdapter noticeListAdapter = null;
    private ArrayList<NoticeData> noticeDataList;
    private NoticeData noticeData;

    private ListView issueListView = null;
    private ListIssueAdapter issueListAdapter = null;
    private ArrayList<IssueData> issueDataList;
    private IssueData issueData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pp_home, container, false);
        noticeDataList = new ArrayList<NoticeData>();
        noticeListView = (ListView)view.findViewById(R.id.list_notice);
        noticeListAdapter = new ListNoticeAdapter(getActivity(), noticeDataList);
        noticeListView.setAdapter(noticeListAdapter);
        getNoticeList();

        issueDataList = new ArrayList<IssueData>();
        issueListView = (ListView)view.findViewById(R.id.list_issue);
        issueListAdapter = new ListIssueAdapter(getActivity(), issueDataList);
        issueListView.setAdapter(issueListAdapter);

        getIssueList();

//        FloatingActionButton button = (FloatingActionButton)view.findViewById(R.id.setter);
//        button.setSize(FloatingActionButton.SIZE_NORMAL);
//        button.setStrokeVisible(false);

        final FloatingActionButton actionA = (FloatingActionButton)view.findViewById(R.id.action_a);
        actionA.setSize(FloatingActionButton.SIZE_MINI);
        actionA.setTitle("add Notice");
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getBaseContext(), "add Notice", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), NoticeAddActivity.class);
                startActivity(intent);
            }
        });

        final FloatingActionButton actionB = (FloatingActionButton)view.findViewById(R.id.action_b);
        actionB.setSize(FloatingActionButton.SIZE_MINI);
        actionB.setTitle("add Issue");
        actionB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getBaseContext(), "B", Toast.LENGTH_LONG).show();
            }
        });

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu)view.findViewById(R.id.multiple_actions);
        //menuMultipleActions.addButton(actionA);
        //menuMultipleActions.addButton(actionB);

        return view;
    }

    public void getNoticeList(){
        class GetNoticeListAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("projectID", ProjectHomeActivity.itemProjectID.toString()));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getNoticeList.php");
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
                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NoticeData p = new NoticeData(jsonObject.getInt("noticeNum"), jsonObject.getString("noticeTitle"),
                                jsonObject.getString("noticeContents"), Date.valueOf(jsonObject.getString("noticeDate")), jsonObject.getString("writer"));
                        noticeListAdapter.addNotice(p);
                        noticeListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        GetNoticeListAsync la = new GetNoticeListAsync();
        la.execute();

    }

    public void getIssueList(){
        class GetIssueListAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("projectID", ProjectHomeActivity.itemProjectID.toString()));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getIssueList.php");
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
                Log.d("--------------SJ11 :", s);
                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        IssueData p = new IssueData(jsonObject.getInt("issueNum"), jsonObject.getString("issueTitle"),
                                jsonObject.getString("issueContents"),jsonObject.getString("issueSolution"), Date.valueOf(jsonObject.getString("issueDate")), jsonObject.getString("writer"));
                        issueListAdapter.addIssue(p);
                        issueListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        GetIssueListAsync la = new GetIssueListAsync();
        la.execute();
    }

}
