package com.example.seonjae.with.dummy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.seonjae.with.R;
import com.example.seonjae.with.data.RequestData;

import java.util.ArrayList;

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
    private RadioButton btnYes;
    private RadioButton btnNo;
    private FButton btnSend;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;

        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.requestlist_item, parent, false);
        }
        projectName = (TextView)convertView.findViewById(R.id.projectName);
        workName = (TextView)convertView.findViewById(R.id.workName);
        requestDate = (TextView)convertView.findViewById(R.id.requestDate);
        btnYes = (RadioButton)convertView.findViewById(R.id.btnYes);
        btnNo = (RadioButton)convertView.findViewById(R.id.btnNo);
        btnSend = (FButton)convertView.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    public void addRequest(RequestData data){
        RequestList.add(data);
    }
}
