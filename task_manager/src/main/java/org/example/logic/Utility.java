package org.example.logic;

import java.util.*;

public class Utility {

    public static List<Employee> filterAndSortEmployees(TaskManagement taskManagement) {
        List<Employee> filteredEmployees = new ArrayList<>();

        for (Map.Entry<Employee, List<Task>> entry : taskManagement.getTaskMap().entrySet()) {
            int workTime = taskManagement.calculateEmployeeWorkDuration(entry.getKey());
            if (workTime > 40) {
                Employee employee = entry.getKey();
                employee.setWorkTime(workTime);
                filteredEmployees.add(employee);
            }
        }

        filteredEmployees.sort(Comparator.comparingInt(taskManagement::calculateEmployeeWorkDuration));

        for (Employee employee : filteredEmployees) {
            System.out.println(employee.getName());
            System.out.println(employee.getWorkTime());
        }

        return filteredEmployees;
    }

    public static Map<String, Map<String, Integer>> countCompletedAndUncompletedTasks(TaskManagement taskManagement) {
        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Map.Entry<Employee, List<Task>> entry : taskManagement.getTaskMap().entrySet()) {
            String employeeName = entry.getKey().getName();

            Map<String, Integer> taskStatusCount = result.computeIfAbsent(employeeName, k -> {
                Map<String, Integer> map = new HashMap<>();
                map.put("Completed", 0);
                map.put("Uncompleted", 0);
                return map;
            });

            for (Task task : entry.getValue()) {
                String status = task.getStatusTask();
                if (!taskStatusCount.containsKey(status)) {
                    taskStatusCount.put(status, 0);
                }
                taskStatusCount.put(status, taskStatusCount.get(status) + 1);
            }
        }

        return result;
    }
}