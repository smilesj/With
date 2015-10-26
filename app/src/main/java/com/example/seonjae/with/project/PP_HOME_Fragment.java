package com.example.seonjae.with.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import android.view.View.OnClickListener;
import android.widget.TextView;
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



        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), ProjectHomeActivity.itemProjectID + " :: " + noticeListAdapter.getItem(position).getNoticeTitle(), Toast.LENGTH_SHORT).show();
                LayoutInflater inflater2 = getActivity().getLayoutInflater();
                final View dialogNoticeView= inflater2.inflate(R.layout.dialog_notice_info, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(noticeListAdapter.getItem(position).getNoticeTitle());
                builder.setView(dialogNoticeView);
                TextView d_noticeContents = (TextView)dialogNoticeView.findViewById(R.id.dialog_notice_contents);
                d_noticeContents.setText(noticeListAdapter.getItem(position).getNoticeContents());
                TextView d_noticeDate = (TextView)dialogNoticeView.findViewById(R.id.dialog_notice_date);
                d_noticeDate.setText(noticeListAdapter.getItem(position).getNoticeDate().toString());
                TextView d_noticeWriter = (TextView)dialogNoticeView.findViewById(R.id.dialog_notice_writer);
                d_noticeWriter.setText(noticeListAdapter.getItem(position).getWriter());
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Action for 'Yes' Button
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        issueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), ProjectHomeActivity.itemProjectID + " :: " + issueListAdapter.getItem(position).getIssueTitle(), Toast.LENGTH_SHORT).show();
                LayoutInflater inflater2 = getActivity().getLayoutInflater();
                final View dialogIssueView= inflater2.inflate(R.layout.dialog_issue_info, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(issueListAdapter.getItem(position).getIssueTitle());
                builder.setView(dialogIssueView);
                TextView d_issueContents = (TextView)dialogIssueView.findViewById(R.id.dialog_issue_contents);
                d_issueContents.setText(issueListAdapter.getItem(position).getIssueContents());
                TextView d_issueSolution = (TextView)dialogIssueView.findViewById(R.id.dialog_issue_solution);
                if(issueListAdapter.getItem(position).getIssueSolution().equals(""))
                    d_issueSolution.setText("-");
                else
                    d_issueSolution.setText(issueListAdapter.getItem(position).getIssueSolution());
                TextView d_issueDate = (TextView)dialogIssueView.findViewById(R.id.dialog_issue_date);
                d_issueDate.setText(issueListAdapter.getItem(position).getIssueDate().toString());
                TextView d_issueWriter = (TextView)dialogIssueView.findViewById(R.id.dialog_issue_writer);
                d_issueWriter.setText(issueListAdapter.getItem(position).getWriter());
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Action for 'Yes' Button
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

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
