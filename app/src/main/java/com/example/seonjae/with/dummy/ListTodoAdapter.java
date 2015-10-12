package com.example.seonjae.with.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        todoName = (TextView)convertView.findViewById(R.id.todoName);
        projectName = (TextView)convertView.findViewById(R.id.projectName);
        endday = (TextView)convertView.findViewById(R.id.endDay);
        tData = getItem(position);

        if(tData != null){
            todoName.setText(TodoList.get(position).getTodoName());
            projectName.setText(TodoList.get(position).getProjectName());
            endday.setText(TodoList.get(position).getEndDay().toString());
        }

        return convertView;

    }

    public void addTodo(TodoData data) { TodoList.add(data); }
}
