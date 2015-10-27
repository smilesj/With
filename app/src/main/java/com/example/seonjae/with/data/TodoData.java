package com.example.seonjae.with.data;

import java.sql.Date;

/**
 * Created by seonjae on 2015-10-12.
 */
public class TodoData {

    private String projectID;
    private String workID;
    private String todoName;
    private String projectName;
    private Date endDay;
    private int priority;
    private int complete;

    public TodoData(String projectID, String workID, String todoName, String projectName, Date endDay, int priority, int complete){
        this.projectID = projectID;
        this.workID = workID;
        this.todoName = todoName;
        this.projectName = projectName;
        this.endDay = endDay;
        this.priority = priority;
        this.complete = complete;
    }

    public String getProjectID() { return projectID; }

    public void setProjectID(String projectID) { this.projectID = projectID; }

    public String getWorkID() { return workID; }

    public void setWorkID(String workID) { this.workID = workID; }

    public String getTodoName() { return todoName; }

    public void setTodoName(String todoName) { this.todoName = todoName; }

    public String getProjectName(){
        return projectName;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    public Date getEndDay() { return endDay;}

    public void setEndDay(Date endDay) { this.endDay = endDay; }

    public int getPriority() { return priority;}

    public void setPriority(int priority) { this.priority = priority; }

    public int getComplete() { return complete;}

    public void setComplete(int complete) { this.complete = complete; }
}
