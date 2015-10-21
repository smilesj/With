package com.example.seonjae.with.data;

/**
 * Created by seonjae on 2015-10-21.
 */
public class WorkerData {

    private String workerEmail;
    private int workerProgress;

    public WorkerData(String workerEmail, int workerProgress){
        this.workerEmail = workerEmail;
        this.workerProgress = workerProgress;
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
}
