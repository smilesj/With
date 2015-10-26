package com.example.seonjae.with.dummy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.seonjae.with.DataConn;
import com.example.seonjae.with.ProjectAddActivity;
import com.example.seonjae.with.R;
import com.example.seonjae.with.data.ProjectData;
import com.example.seonjae.with.project.ProjectHomeActivity;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seonjae on 2015-10-05.
 */
public class MP_Project_Fragment extends Fragment {

    private View view;
    private ListView pListView = null;
    private ListProjectAdapter pListAdapter = null;
    private ArrayList<ProjectData> pDataList;
    private ProjectData pData;
    private ArrayList<String> projectNameList;
    private Map<String,String> projectInfo;
    private DataConn dataConn;
    static public Map<String, Map<String, String>> team;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        team = new HashMap<String, Map<String,String>>();
        dataConn = new DataConn();
        projectNameList = new ArrayList<String>();
        projectInfo = new HashMap<String, String>();
        pDataList = new ArrayList<ProjectData>();
        view = inflater.inflate(R.layout.mp_project_fragment, container, false);
        pListView = (ListView)view.findViewById(R.id.project_list);
        pListView.setDivider(null);
        pListView.setDividerHeight(5);
        pListAdapter = new ListProjectAdapter(getActivity(), pDataList);
        pListView.setAdapter(pListAdapter);

        getProjectList();

        pListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), pListAdapter.getItem(position).getProjectName()+" :: "+ pListAdapter.getItem(position).getProjectID(), Toast.LENGTH_SHORT).show();
//                Bundle bundle = new Bundle();
//                bundle.putString("projectID", pListAdapter.getItem(position).getProjectID());
//                bundle.putString("projectName", pListAdapter.getItem(position).getProjectName());
//                ProjectAddActivity projectAddActivity = new ProjectAddActivity();

                Intent intent = new Intent(getActivity(), ProjectHomeActivity.class);
                intent.putExtra("projectID", pListAdapter.getItem(position).getProjectID());
                intent.putExtra("projectName", pListAdapter.getItem(position).getProjectName());
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProjectAddActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        getTeamWorkerList();

        return view;
    }

    private void getProjectList() {
        class GetProjectListAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String t_email = "test@mail.com";

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", t_email));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getProjectList.php");
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
                Log.d("--------------SJ 5 :" , s);
                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ProjectData p = new ProjectData(jsonObject.getString("projectID"), jsonObject.getString("projectName"));
                        projectNameList.add(jsonObject.getString("projectName"));
                        projectInfo.put(jsonObject.getString("projectID"),jsonObject.getString("projectName"));
                        pListAdapter.addProject(p);
                        pListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataConn.setProjectNameList(projectNameList);
                dataConn.setProjectInfo(projectInfo);
            }

        }
        GetProjectListAsync la = new GetProjectListAsync();
        la.execute();
    }

    private void getTeamWorkerList() {
        class GetTeamWorkerListAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String t_email = "test@mail.com";

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", t_email));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getTeamWorkerList.php");
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
                Log.d("--------------SJ17 :" , s);
                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(!team.containsKey(jsonObject.getString("projectID"))){
                            Map<String, String> workers = new HashMap<String, String>();
                            workers.put(jsonObject.getString("email"), jsonObject.getString("regID"));
                            team.put(jsonObject.getString("projectID"), workers);
                        }
                        else{
                            Map<String, String> workers = team.get(jsonObject.getString("projectID"));
                            workers.put(jsonObject.getString("email"), jsonObject.getString("regID"));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        GetTeamWorkerListAsync la = new GetTeamWorkerListAsync();
        la.execute();
    }
}
