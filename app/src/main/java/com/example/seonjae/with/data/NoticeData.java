package com.example.seonjae.with.data;

import java.util.Date;

/**
 * Created by seonjae on 2015-10-19.
 */
public class NoticeData {

    private int noticeNum;
    private String noticeTitle;
    private String noticeContents;
    private Date noticeDate;
    private String writer;
    public NoticeData(int noticeNum, String noticeTitle, String noticeContents, Date noticeDate, String writer){
        this.noticeNum = noticeNum;
        this.noticeTitle = noticeTitle;
        this.noticeContents = noticeContents;
        this.noticeDate = noticeDate;
        this.writer = writer;
    }

    public int getNoticeNum(){
        return noticeNum;
    }

    public void setNoticeNum(int noticeNum){
        this.noticeNum = noticeNum;
    }

    public String getNoticeTitle(){
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle){
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContents(){
        return noticeContents;
    }

    public void setNoticeContents(String noticeContents){
        this.noticeContents = noticeContents;
    }

    public Date getNoticeDate(){
        return noticeDate;
    }

    public void setNoticeDate(Date noticeDate){
        this.noticeDate = noticeDate;
    }

    public String getWriter(){
        return  writer;
    }

    public void setWriter(String writer){
        this.writer = writer;
    }

}
