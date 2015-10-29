package com.example.seonjae.with.dummy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seonjae.with.R;
import com.example.seonjae.with.data.ProjectData;

import java.util.ArrayList;

/**
 * Created by seonjae on 2015-10-05.
 */
@SuppressLint("ViewHolder")
public class ListProjectAdapter extends BaseAdapter {

    private Context context;
    private ProjectData pData;

    private LinearLayout projectColor;
    private TextView projectName;
    private ArrayList<ProjectData> ProjectList;

    public ListProjectAdapter(Context context, ArrayList<ProjectData> ProjectList){
        super();
        this.context = context;
        this.ProjectList = ProjectList;
    }
    @Override
    public int getCount() {
        return ProjectList.size();
    }

    @Override
    public ProjectData getItem(int position) {
        return ProjectList.get(position);
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
            convertView = inflater.inflate(R.layout.projectlist_item, parent, false);
        }
        projectColor = (LinearLayout)convertView.findViewById(R.id.projectColor);
        projectName = (TextView)convertView.findViewById(R.id.projectName);
        pData = getItem(position);

        if(pData != null){
            projectName.setText(ProjectList.get(position).getProjectName());
            int red = Color.red(ProjectList.get(position).getProjectColor());
            int green = Color.green(ProjectList.get(position).getProjectColor());
            int blue = Color.blue(ProjectList.get(position).getProjectColor());
            projectColor.setBackgroundColor(Color.rgb(red, green, blue));
        }

        return convertView;
    }

    public void addProject(ProjectData data){
        ProjectList.add(data);
    }

}
