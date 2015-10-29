package com.example.seonjae.with.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.seonjae.with.R;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

/**
 * Created by seonjae on 2015-10-29.
 */
public class ListSearchAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> SearchList;
    private String searchData;

    private TextView searchEmail;
    private FButton deleteEmail;

    public ListSearchAdapter(Context context, ArrayList<String> SearchList){
        super();
        this.context = context;
        this.SearchList = SearchList;
    }
    @Override
    public int getCount() {
        return SearchList.size();
    }

    @Override
    public String getItem(int position) {
        return SearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        if(convertView == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchlist_item, parent, false);
        }
        searchEmail = (TextView)convertView.findViewById(R.id.searchEmail);
        deleteEmail = (FButton)convertView.findViewById(R.id.deleteEmail);
        searchData = getItem(position);
        if(searchData!=null){
            searchEmail.setText(SearchList.get(position).toString());
            deleteEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchList.remove(position);
                }
            });
        }
        return convertView;
    }

    public void addSearch(String data) { SearchList.add(data); }
}
