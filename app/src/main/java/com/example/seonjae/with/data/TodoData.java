package com.example.seonjae.with.data;

import java.sql.Date;

/**
 * Created by seonjae on 2015-10-12.
 */
public class TodoData {

    private String workID;
    private String todoName;
    private String projectName;
    private Date endDay;

    public TodoData(String workID, String todoName, String projectName, Date endDay){
        this.workID = workID;
        this.todoName = todoName;
        this.projectName = projectName;
        this.endDay = endDay;
    }

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
}
