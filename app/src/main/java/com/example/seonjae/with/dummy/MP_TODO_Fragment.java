package com.example.seonjae.with.dummy;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ListView;

import com.example.seonjae.with.R;
import com.example.seonjae.with.TodoAddActivity;
import com.example.seonjae.with.data.TodoData;

import java.util.ArrayList;

public class MP_TODO_Fragment extends Fragment {

    private View view;
    private ListView tListView = null;
    private ListTodoAdapter tListAdapter = null;
    private ArrayList<TodoData> tDataList;
    private TodoData pData;

    public MP_TODO_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tDataList = new ArrayList<TodoData>();
        view = inflater.inflate(R.layout.mp_todo_fragment, container, false);
        tListView = (ListView) view.findViewById(R.id.todo_list);
        tListView.setDivider(null);
        tListView.setDividerHeight(5);
        tListAdapter = new ListTodoAdapter(getActivity(), tDataList);
        tListView.setAdapter(tListAdapter);

        getTodoList();

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodoAddActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return view;
    }

    private void getTodoList() {
        class GetTodoListAsync extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String t_email = "test@mail.com";
                return null;
            }
        }
    }
}


