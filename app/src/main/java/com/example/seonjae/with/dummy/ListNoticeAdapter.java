package com.example.seonjae.with.dummy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.seonjae.with.R;
import com.example.seonjae.with.data.NoticeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seonjae on 2015-10-19.
 */
public class ListNoticeAdapter extends BaseAdapter {

    private Context context;
    private NoticeData noticeData;
    private ArrayList<NoticeData> NoticeList;

    private TextView noticeTitle;
    private TextView noticeContents;
    private TextView noticeDate;

    public ListNoticeAdapter(Context context, ArrayList<NoticeData> NoticeList){
        super();
        this.context = context;
        this.NoticeList = NoticeList;
    }

    @Override
    public int getCount() {
        return NoticeList.size();
    }

    @Override
    public NoticeData getItem(int position) {
        return NoticeList.get(position);
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
            convertView = inflater.inflate(R.layout.noticelist_item, parent, false);
        }
        noticeTitle = (TextView)convertView.findViewById(R.id.noticeTitle);
        noticeContents = (TextView)convertView.findViewById(R.id.noticeContents);
        noticeData = getItem(position);
        noticeDate = (TextView)convertView.findViewById(R.id.noticeDate);

        if(noticeData != null){
            noticeTitle.setText(NoticeList.get(position).getNoticeTitle());
            noticeContents.setText(NoticeList.get(position).getNoticeContents());
            noticeDate.setText(NoticeList.get(position).getNoticeDate().toString());
        }
        return convertView;
    }
    public void addNotice(NoticeData data) { NoticeList.add(data); }
}
