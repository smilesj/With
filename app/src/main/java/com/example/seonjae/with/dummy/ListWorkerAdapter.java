package com.example.seonjae.with.dummy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.example.seonjae.with.R;
import com.example.seonjae.with.data.WorkerData;

import java.util.ArrayList;

/**
 * Created by seonjae on 2015-10-21.
 */
public class ListWorkerAdapter extends BaseAdapter {

    private Context context;
    private WorkerData workerData;
    private ArrayList<WorkerData> WorkerList;

    private IconRoundCornerProgressBar progressWorker;

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

        progressWorker = (IconRoundCornerProgressBar)convertView.findViewById(R.id.progressWorker);
        workerData = getItem(position);

        if(workerData != null){
            progressWorker.setProgressColor(Color.parseColor("#56d2c2"));
            progressWorker.setBackgroundColor(Color.parseColor("#757575"));
            progressWorker.setMax(100);
            progressWorker.setProgress(70); //WorkerList.get(position).getWorkerProgress();
        }
        return convertView;
    }

    public void addWorker(WorkerData data) { WorkerList.add(data); }
}
