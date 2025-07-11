package org.example.logic;

import java.util.ArrayList;

public final class ComplexTask extends Task {
    private ArrayList<Task> listTasks=new ArrayList<>();
    public ComplexTask(int id,String status){
        super(id,status);
    }
    @Override
    public int estimateDuration() {
        int time = 0;
        for (Task task : listTasks) {
            time += task.estimateDuration();
        }
        return time;
    }

    public void addTask(Task task) {
        listTasks.add(task);
    }

    public void deleteTask(Task task) {
        listTasks.remove(task);
    }

    public  ArrayList<Task> getListTasks() {
        return listTasks;
    }

}
