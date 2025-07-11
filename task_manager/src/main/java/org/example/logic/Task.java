package org.example.logic;

import java.io.Serializable;

public sealed abstract class Task implements Serializable permits SimpleTask, ComplexTask {
    private Integer idTask;
    private String statusTask;
    public Task(int id,String status){
        this.idTask=id;
        this.statusTask=status;
    }
    public int getIdTask(){
        return this.idTask;
    }
    public String getStatusTask(){
        return this.statusTask;
    }
    public void setStatusTask(String status){
        this.statusTask=status;
    }
    public abstract int  estimateDuration();

}
