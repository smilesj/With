package com.example.seonjae.with.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seonjae on 2015-10-05.
 */
public class ProjectData {
    private String projectID;
    private String projectName;
    private int projectColor;

    public ProjectData(String projectID, String projectName, int projectColor){
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectColor = projectColor;
    }

    public String getProjectID() { return projectID; }

    public void SetProjectID(String projectID) { this.projectID = projectID; }

    public String getProjectName(){
        return projectName;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    public int getProjectColor() {
        return projectColor;
    }

    public void setProjectColor(int projectColor) {
        this.projectColor = projectColor;
    }
}
