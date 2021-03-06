package com.example.seonjae.with.dummy;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.seonjae.with.R;
import com.example.seonjae.with.data.WorkerData;
import com.example.seonjae.with.project.PP_Progress_Fragment;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.ArrayList;

/**
 * Created by seonjae on 2015-10-21.
 */
public class ListWorkerAdapter extends BaseAdapter {

    private Context context;
    private WorkerData workerData;
    private ArrayList<WorkerData> WorkerList;

    private RoundCornerProgressBar progressWorker;
    private TextView worker;
    private TextView progressNum;

    public ListWorkerAdapter(Context context, ArrayList<WorkerData> WorkerList){
        super();
        this.context = context;
        this.WorkerList = WorkerList;
    }

    @Override
    public int getCount() {
        return WorkerList.size();
    }

    @Override
    public WorkerData getItem(int position) {
        return WorkerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = null;
        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.workerlist_item, parent, false);
        }

        progressWorker = (RoundCornerProgressBar)convertView.findViewById(R.id.progressWorker);
        worker = (TextView)convertView.findViewById(R.id.worker);
        progressNum = (TextView)convertView.findViewById(R.id.progressNum);

        workerData = getItem(position);

        if(workerData != null){
            if((position%2) == 0){
                progressWorker.setProgressColor(Color.rgb(145,142,219));
            }
            progressWorker.setProgress((float) WorkerList.get(position).getWorkerProgress());
            worker.setText(workerData.getWorkerEmail());
            progressNum.setText(String.valueOf((int) progressWorker.getProgress()) + "%");
        }

//        if(workerData != null){
//            PP_Progress_Fragment.progressAllValue = WorkerList.get(position).getAllProgress();
//            progressWorker.setProgressColor(Color.parseColor("#56d2c2"));
//            progressWorker.setBackgroundColor(Color.parseColor("#757575"));
//            progressWorker.setMax(100);
//            progressWorker.setProgress(WorkerList.get(position).getWorkerProgress());
//        }

        return convertView;
    }

    public void addWorker(WorkerData data) { WorkerList.add(data); }
}
