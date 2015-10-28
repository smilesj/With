package com.example.seonjae.with.dummy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.seonjae.with.R;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MP_Request_Fragment extends Fragment {

    private View view;
    private ListView rListView = null;
    private ListRequestAdapter rListAdapter = null;
    private ArrayList<RequestData> rDataList;
    private RequestData rData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rDataList = new ArrayList<RequestData>();
        view = inflater.inflate(R.layout.fragment_mp_request, container, false);
        rListView = (ListView)view.findViewById(R.id.request_list);
        rListView.setDivider(null);
        rListView.setDividerHeight(5);
        rListAdapter = new ListRequestAdapter(getActivity(), rDataList);
        rListView.setAdapter(rListAdapter);

        getRequestList();
        return view;
    }

    private void getRequestList(){
        class GetRequestListAsync extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String t_email = "test@mail.com";

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("worker", t_email));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getRequestList.php");
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
                Log.d("--------------SJ 5 :", s);
                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        RequestData p = new RequestData(jsonObject.getString("workID"), jsonObject.getString("worker"), jsonObject.getString("workName")
                                , jsonObject.getString("projectName"), Date.valueOf(jsonObject.getString("requestDate")), jsonObject.getString("writer"));
                        rListAdapter.addRequest(p);
                        rListAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        GetRequestListAsync la = new GetRequestListAsync();
        la.execute();
    }



}
