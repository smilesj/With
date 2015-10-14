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
import android.widget.ListView;

import com.example.seonjae.with.R;
import com.example.seonjae.with.TodoAddActivity;
import com.example.seonjae.with.data.ProjectData;
import com.example.seonjae.with.data.TodoData;

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

public class MP_TODO_Fragment extends Fragment {

    private View view;
    private ListView tListView = null;
    private ListTodoAdapter tListAdapter = null;
    private ArrayList<TodoData> tDataList;
    private TodoData pData;

    public MP_TODO_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        tDataList = new ArrayList<TodoData>();
        view = inflater.inflate(R.layout.mp_todo_fragment, container, false);
        tListView = (ListView) view.findViewById(R.id.todo_list);
        tListView.setDivider(null);
        tListView.setDividerHeight(5);
        tListAdapter = new ListTodoAdapter(getActivity(), tDataList);
        tListView.setAdapter(tListAdapter);

        getTodoList();

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodoAddActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return view;
    }

    private void getTodoList() {
        class GetTodoListAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String t_email = "test@mail.com";

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("email", t_email));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getTodoList.php");
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
                Log.d("--------------SJ 5 :", s);
                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TodoData p = new TodoData(jsonObject.getString("workName"),jsonObject.getString("projectName"), Date.valueOf(jsonObject.getString("endDay")));
                        tListAdapter.addTodo(p);
                        tListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        GetTodoListAsync la = new GetTodoListAsync();
        la.execute();
    }
}


