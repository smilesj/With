package com.example.seonjae.with.dummy;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.seonjae.with.R;
import com.example.seonjae.with.data.TodoData;

import java.util.ArrayList;

/**
 * Created by seonjae on 2015-10-12.
 */
public class ListTodoAdapter extends BaseAdapter {

    private Context context;
    private TodoData tData;

    private LinearLayout projectColor;
    private TextView todoName;
    private TextView projectName;
    private TextView endday;
    private ArrayList<TodoData> TodoList;

    public ListTodoAdapter(Context context, ArrayList<TodoData> TodoList){
        super();
        this.context = context;
        this.TodoList = TodoList;
    }

    @Override
    public int getCount() {
        return TodoList.size();
    }

    @Override
    public TodoData getItem(int position) {
        return TodoList.get(position);
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
            convertView = inflater.inflate(R.layout.todolist_item, parent, false);
        }
        projectColor = (LinearLayout)convertView.findViewById(R.id.projectColor);
        todoName = (TextView)convertView.findViewById(R.id.todoName);
        projectName = (TextView)convertView.findViewById(R.id.projectName);
        endday = (TextView)convertView.findViewById(R.id.endDay);
        tData = getItem(position);

        if(tData != null){
            if(tData.getComplete() == 1){
                projectColor.setBackgroundColor(Color.rgb(207, 207, 207));
            }
            else {
                if ((position % 2) == 0)
                    projectColor.setBackgroundColor(Color.rgb(232, 217, 255));
                    //projectColor.setBackgroundColor(Color.rgb(209, 178, 255));
                else
                    projectColor.setBackgroundColor(Color.rgb(255, 217, 236));
                //projectColor.setBackgroundColor(Color.rgb(255, 178, 217));
            }
            todoName.setText(TodoList.get(position).getTodoName());
            projectName.setText(TodoList.get(position).getProjectName());
            endday.setText(TodoList.get(position).getEndDay().toString());
        }

        return convertView;

    }

    public void addTodo(TodoData data) { TodoList.add(data); }
}
