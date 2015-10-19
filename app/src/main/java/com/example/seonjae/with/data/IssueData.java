package com.example.seonjae.with.data;

import java.util.Date;

/**
 * Created by seonjae on 2015-10-19.
 */
public class IssueData {
    private int issueNum;
    private String issueTitle;
    private String issueContents;
    private String issueSolution;
    private Date issueDate;
    private String writer;

    public IssueData(int issueNum, String issueTitle, String issueContents,String issueSolution, Date issueDate, String writer){
        this.issueNum = issueNum;
        this.issueTitle = issueTitle;
        this.issueContents = issueContents;
        this.issueSolution = issueSolution;
        this.issueDate = issueDate;
        this.writer = writer;
    }

    public int getIssueNum(){
        return issueNum;
    }

    public void setIssueNum(int issueNum){
        this.issueNum = issueNum;
    }

    public String getIssueTitle(){
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle){
        this.issueTitle = issueTitle;
    }

    public String getIssueContents(){
        return issueContents;
    }

    public void setIssueContents(String issueContents){
        this.issueContents = issueContents;
    }

    public String getIssueSolution(){
        return issueSolution;
    }

    public void setIssueSolution(String issueSolution){
        this.issueSolution = issueSolution;
    }

    public Date getIssueDate(){
        return issueDate;
    }

    public void setIssueDate(Date issueDate){
        this.issueDate = issueDate;
    }

    public String getWriter(){
        return  writer;
    }

    public void setWriter(String writer){
        this.writer = writer;
    }
}
