package com.example.seonjae.with.project;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.example.seonjae.with.R;
import com.example.seonjae.with.data.NoticeData;
import com.example.seonjae.with.data.WorkerData;
import com.example.seonjae.with.dummy.ListWorkerAdapter;
import com.github.lzyzsd.circleprogress.ArcProgress;

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

public class PP_Progress_Fragment extends Fragment {

    private View view;
    private ListView workerListView = null;
    private ListWorkerAdapter workerListAdapter = null;
    private ArrayList<WorkerData> workerDataList;
    private WorkerData workerData;
    private ArcProgress progressAll;
    static public int progressAllValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pp_progress, container, false);
        workerDataList = new ArrayList<WorkerData>();
        workerListView = (ListView)view.findViewById(R.id.list_worker);
        workerListView.setDivider(null);
        workerListView.setDividerHeight(5);
        workerListAdapter = new ListWorkerAdapter(getActivity(), workerDataList);
        workerListView.setAdapter(workerListAdapter);
        getWorkerList();

        progressAll = (ArcProgress)view.findViewById(R.id.progressAll);
        progressAll.setProgress(progressAllValue);
        return view;
    }

    private void getWorkerList(){
        class GetWorkerListAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("projectID", ProjectHomeActivity.itemProjectID.toString()));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://with7.cloudapp.net/getWorkerList.php");
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
                Log.d("------------SJ12: ", s);
                final String json = s.replaceAll("\"", "\\\"");
                try{
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        WorkerData p = new WorkerData(jsonObject.getString("workerEmail"), jsonObject.getInt("workerProgress"), jsonObject.getInt("allProgress"));
                        workerListAdapter.addWorker(p);
                        workerListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        GetWorkerListAsync la = new GetWorkerListAsync();
        la.execute();
    }

}
