package com.example.seonjae.with.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seonjae.with.R;
import com.example.seonjae.with.data.IssueData;

import java.util.ArrayList;

/**
 * Created by seonjae on 2015-10-19.
 */
public class ListIssueAdapter extends BaseAdapter {

    private Context context;
    private IssueData issueData;
    private ArrayList<IssueData> IssueList;

    private TextView issueTitle;
    private TextView issueDate;
    private TextView issueComplete;

    public ListIssueAdapter(Context context, ArrayList<IssueData> IssueList){
        super();
        this.context = context;
        this.IssueList = IssueList;
    }
    @Override
    public int getCount() {
        return IssueList.size();
    }

    @Override
    public IssueData getItem(int position) {
        return IssueList.get(position);
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
            convertView = inflater.inflate(R.layout.issuelist_item, parent, false);
        }
        issueTitle = (TextView)convertView.findViewById(R.id.issueTitle);
        issueDate = (TextView)convertView.findViewById(R.id.issueDate);
        issueComplete = (TextView)convertView.findViewById(R.id.issueComplete);
        issueData = getItem(position);

        if(issueData != null){
            issueTitle.setText(IssueList.get(position).getIssueTitle());
            issueDate.setText(String.valueOf(IssueList.get(position).getIssueDate()));
            if(IssueList.get(position).getIssueSolution().equals(""))
                issueComplete.setText("X");
            else
                issueComplete.setText("O");
        }

        return convertView;
    }

    public void addIssue(IssueData data) { IssueList.add(data); }
}
