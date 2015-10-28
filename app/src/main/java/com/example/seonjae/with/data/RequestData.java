package com.example.seonjae.with.data;

import java.util.Date;

/**
 * Created by seonjae on 2015-10-29.
 */
public class RequestData {
    private String workID;
    private String worker;
    private String workName;
    private String projectName;
    private Date requestDate;
    private String writer;

    public RequestData(String workID, String worker, String workName, String projectName, Date requestDate, String writer){
        this.workID = workID;
        this.worker = worker;
        this.workName = workName;
        this.projectName = projectName;
        this.requestDate = requestDate;
        this.writer = writer;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }


}
