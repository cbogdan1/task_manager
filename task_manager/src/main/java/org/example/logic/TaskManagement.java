package org.example.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagement implements Serializable {
    private Map<Employee, List<Task>> taskMap=new HashMap<>();
    private Map<Integer, Task> taskMapMang = new HashMap<>();

    public void assignTaskToEmployee(Employee employee, Task task) {
        taskMap.putIfAbsent(employee,new ArrayList<>());
        taskMap.get(employee).add(task);
        int i=taskMap.get(employee).indexOf(task);
        taskMap.get(employee).get(i).setStatusTask("assigned to:"+employee.getName());
    }
    public void addEmployee(Employee employee) {
        taskMap.putIfAbsent(employee, new ArrayList<>());
    }
    public void modifyTaskStatus(Employee employee, Task task, String status) {
        if (!taskMap.containsKey(employee)) {
            System.out.println("Employee not found in taskMap!");
            return;
        }

        List<Task> employeeTasks = taskMap.get(employee);
        Task foundTask = null;

        for (Task t : employeeTasks) {
            if (t.getIdTask() == task.getIdTask()) {
                foundTask = t;
                break;
            }
        }

        if (foundTask == null) {
            System.out.println("Task not found for the given Employee!");
            return;
        }

        foundTask.setStatusTask(status);

        if (taskMapMang.containsKey(task.getIdTask())) {
            taskMapMang.get(task.getIdTask()).setStatusTask(status);
        }

        System.out.println("Task status updated successfully: " + status);
    }

    public int calculateEmployeeWorkDuration(Employee employee){
        int workTime=0;
        if (!taskMap.containsKey(employee)) {
            return 0;
        }
        for (Task task : taskMap.getOrDefault(employee, new ArrayList<>())) {
            if ("Completed".equals(task.getStatusTask())) {
                workTime += task.estimateDuration();
            }
        }
        return workTime;
    }
    public Map<Employee, List<Task>> getTaskMap() {
        return taskMap;
    }
    public Map<Integer, Task> getTaskMapMang() {
        return taskMapMang;
    }

}
