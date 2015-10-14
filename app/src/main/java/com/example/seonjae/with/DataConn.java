package com.example.seonjae.with;

import java.util.ArrayList;

/**
 * Created by seonjae on 2015-10-14.
 */
public class DataConn {
    static private ArrayList<String> projectNameList;

    public DataConn(){
        super();
        //projectNameList = new ArrayList<String>();
        //projectNameList = getProjectNameList();
    }

    public ArrayList<String> getProjectNameList(){
        return this.projectNameList;
    }

    public void setProjectNameList(ArrayList<String> projectNameList){
        this.projectNameList = projectNameList;
    }
}
