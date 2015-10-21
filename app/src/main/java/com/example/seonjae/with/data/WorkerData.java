package com.example.seonjae.with.data;

/**
 * Created by seonjae on 2015-10-21.
 */
public class WorkerData {

    private String workerEmail;
    private int workerProgress;
    private int allProgress;

    public WorkerData(String workerEmail, int workerProgress, int allProgress){
        this.workerEmail = workerEmail;
        this.workerProgress = workerProgress;
        this.allProgress = allProgress;
    }

    public String getWorkerEmail(){
        return workerEmail;
    }

    public void setWorkerEmail(String workerEmail){
        this.workerEmail = workerEmail;
    }

    public int getWorkerProgress(){
        return workerProgress;
    }

    public void setWorkerProgress(int workerProgress){
        this.workerProgress = workerProgress;
    }

    public int getAllProgress(){
        return allProgress;
    }

    public void setAllProgress(int allProgress){
        this.allProgress = allProgress;
    }
}
