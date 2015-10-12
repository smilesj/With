package com.example.seonjae.with.data;

import java.sql.Date;

/**
 * Created by seonjae on 2015-10-12.
 */
public class TodoData {

    private String todoName;
    private String projectName;
    private Date endDay;

    public TodoData(String todoName, String projectName, Date endDay){
        this.todoName = todoName;
        this.projectName = projectName;
        this.endDay = endDay;
    }

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
