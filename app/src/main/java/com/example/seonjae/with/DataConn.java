package com.example.seonjae.with;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by seonjae on 2015-10-14.
 */
public class DataConn {
    static private ArrayList<String> projectNameList;
    static private Map<String, String> projectInfo;

    public DataConn(){
        super();
    }

    public ArrayList<String> getProjectNameList(){
        return this.projectNameList;
    }

    public void setProjectNameList(ArrayList<String> projectNameList){
        this.projectNameList = projectNameList;
    }

    public Map<String,String> getProjectInfo(){
        return this.projectInfo;
    }

    public void setProjectInfo(Map<String,String> projectInfo){
        this.projectInfo = projectInfo;
    }
}
