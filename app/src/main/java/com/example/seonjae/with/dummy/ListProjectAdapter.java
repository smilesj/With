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
import java.util.List;
import java.util.Vector;

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

    private ArrayList<Integer[]> colorList;

    public ListProjectAdapter(Context context, ArrayList<ProjectData> ProjectList){
        super();
        this.context = context;
        this.ProjectList = ProjectList;
    }

    private void addColor(){
        colorList = new ArrayList<Integer[]>();
        Integer[] rgb = new Integer[3];
        rgb[0] = 255;
        rgb[1] = 232;
        rgb[2] = 255;
        colorList.add(rgb);
        rgb[1] = 214;
        colorList.add(rgb);
        rgb[1] = 196;
        colorList.add(rgb);
        rgb[1] = 178;
        rgb[2] = 245;
        colorList.add(rgb);
        rgb[0] = 237;
        rgb[1] = 160;
        rgb[2] = 227;
        colorList.add(rgb);
        rgb[0] = 219;
        rgb[1] = 142;
        rgb[2] = 209;
        colorList.add(rgb);
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
        addColor();
        int colorSize = colorList.size();
        int colorCnt = 0;
        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.projectlist_item, parent, false);
        }
        projectColor = (LinearLayout)convertView.findViewById(R.id.projectColor);
        projectName = (TextView)convertView.findViewById(R.id.projectName);
        pData = getItem(position);

        if(pData != null){
            projectName.setText(ProjectList.get(position).getProjectName());
//            int red = Color.red(ProjectList.get(position).getProjectColor());
//            int green = Color.green(ProjectList.get(position).getProjectColor());
//            int blue = Color.blue(ProjectList.get(position).getProjectColor());
//            Integer[] colors = new Integer[3];
//            colors = colorList.get(colorCnt);
//            projectColor.setBackgroundColor(Color.rgb(colors[0], colors[1], colors[2]));
//            if(colorCnt<=colorSize)
//                colorCnt++;
//            else
//                colorCnt=0;
            if( (position%2) == 0)
                projectColor.setBackgroundColor(Color.rgb(232, 217, 255));
                //projectColor.setBackgroundColor(Color.rgb(209, 178, 255));
            else
                projectColor.setBackgroundColor(Color.rgb(250, 244, 192));
            //projectColor.setBackgroundColor(Color.rgb(255, 178, 217));
        }

        return convertView;
    }

    public void addProject(ProjectData data){
        ProjectList.add(data);
    }

}
