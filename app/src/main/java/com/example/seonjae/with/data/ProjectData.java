package com.example.seonjae.with.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seonjae on 2015-10-05.
 */
public class ProjectData {
    private String projectID;
    private String projectName;

    public ProjectData(String projectID, String projectName){
        this.projectID = projectID;
        this.projectName = projectName;
    }

    public String getProjectID() { return projectID; }

    public void SetProjectID(String projectID) { this.projectID = projectID; }

    public String getProjectName(){
        return projectName;
    }

    public void setProjectName(String projectName){
        this.projectName = projectName;
    }
}
