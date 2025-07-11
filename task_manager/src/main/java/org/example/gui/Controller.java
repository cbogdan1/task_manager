package org.example.gui;

import org.example.logic.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller implements ActionListener {
    private View view;
    private static final String EMPLOYEE_FILE_NAME = "employees.dat";
    private static final String TASK_FILE_NAME = "tasks.dat";
    private static final String TASK_MANAGEMENT_FILE="task_management.data";
    private List<Task> Tasks = new ArrayList<>();
    private Map<Integer, Task> taskMap = new HashMap<>();
    private List<Employee> Employees = new ArrayList<>();
    private Map<Integer, Employee> employeeMap = new HashMap<>();
    private TaskManagement employeeManagement=new TaskManagement();

    public Controller(View view) {

        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("addEmployee")) {
            addEmployee();
        }
        if (command.equals("addTask")) {
            addTask();
        }
        if (command.equals("createComplexTask")) {
            createComplexTask();
        }
        if(command.equals("removeTask")){
            removeTaskFromComplexTask();
        }
        if(command.equals("addTaskToComplex")){
            addTaskToComplexTask();
        }
        if(command.equals("assignTask")){
            assignTaskToEmployee();
        }
        if(command.equals("EmployeeWorkDuration")){
            calculateEmployeeWork();
        }
        if(command.equals("modifyTaskStatus")){
            modifyTaskStatus();
        }
        if(command.equals("filterAndSort")){
            filterAndSortEmployees();
        }
        if(command.equals("completedTasks")){
            countCompletedAndUncompletedTasks();
        }

    }

    private void addEmployee() {
        String idText = view.getIdField().getText();
        String name = view.getNameField().getText();

        if (idText.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Introduceți ID și nume!", "Eroare", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {

            int id = Integer.parseInt(idText);

            Employee e1= new Employee(id,name);

            employeeManagement.addEmployee(e1); //taskManagement
            Employees.add(e1); //Lista Employees
            employeeMap.put(e1.getId(),e1); // Mapa Employees /ID


            DefaultTableModel model = (DefaultTableModel) view.getEmployeeTable().getModel();
            model.addRow(new Object[]{e1.getId(), e1.getName(),e1.getWorkTime()});

            view.getIdField().setText("");
            view.getNameField().setText("");

            saveTaskManagement();
            saveEmployees();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "ID-ul trebuie să fie un numar!", "Eroare", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void addTask() {
        String taskId = view.getSimpleTaskIdField().getText();
        String taskStatus = view.getSimpleTaskStatusField().getText();
        String startHour = view.getStartHourField().getText();
        String endHour = view.getEndHourField().getText();

        if (taskId.isEmpty() || taskStatus.isEmpty() || startHour.isEmpty() || endHour.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Campurile Task ID , Task Status, Start Hour si End Hour nu trb sa fie goale ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int start = Integer.parseInt(startHour);
            int end = Integer.parseInt(endHour);
            int id = Integer.parseInt(taskId);
            if (start < 0 || start > 23 || end < 0 || end > 23 || start >= end) {
                JOptionPane.showMessageDialog(view, "Ore de lucru intre 0-23", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Task task = new SimpleTask(id, taskStatus, start, end);

            Tasks.add(task);//Lista Taskuri
            this.taskMap.put(task.getIdTask(),task); //Mapa taskuri /ID
            this.employeeManagement.getTaskMapMang().put(task.getIdTask(),task);

            DefaultTableModel model = (DefaultTableModel) view.getTaskTable().getModel();
            model.addRow(new Object[]{task.getIdTask(), task.getStatusTask(), task.estimateDuration()});

            view.getSimpleTaskIdField().setText("");
            view.getSimpleTaskStatusField().setText("");
            view.getStartHourField().setText("");
            view.getEndHourField().setText("");
            saveTasks();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Orele de lucru trb sa fie nr de tip int", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void createComplexTask() {
        String idText = view.getComplexIdField().getText();
        String tasksInput = view.getComplexTaskField().getText();

        if (idText.isEmpty() || tasksInput.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Campurile  Complex Task ID si Task IDs! nu trebuie sa fie goale", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int complexTaskId = Integer.parseInt(idText.trim());
            String[] ids = tasksInput.split(",");
            List<Task> selectedTasks = new ArrayList<>();
            List<Integer> taskIds = new ArrayList<>();

            for (String idStr : ids) {
                int id = Integer.parseInt(idStr.trim());
                for (Task task : Tasks) {
                    if (task.getIdTask() == id) {
                        selectedTasks.add(task);
                        taskIds.add(id);
                        break;
                    }
                }
            }

            if (selectedTasks.size() < 2) {
                JOptionPane.showMessageDialog(view, "Selectati minim 2 Taskrui", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ComplexTask complexTask = new ComplexTask(complexTaskId, "Complex Task");
            for (Task task : selectedTasks) {
                complexTask.addTask(task);
            }

            DefaultTableModel complexTaskModel = (DefaultTableModel) view.getComplexTaskTable().getModel();
            complexTaskModel.addRow(new Object[]{complexTask.getIdTask(), taskIds.toString(), complexTask.estimateDuration()});
            DefaultTableModel model = (DefaultTableModel) view.getTaskTable().getModel();
            model.addRow(new Object[]{complexTask.getIdTask(), complexTask.getStatusTask(), complexTask.estimateDuration()});

            Tasks.add(complexTask);
            this.taskMap.put(complexTask.getIdTask(),complexTask);
            this.employeeManagement.getTaskMapMang().put(complexTask.getIdTask(),complexTask);

            saveTasks();
            saveTaskManagement();
            view.getComplexTaskField().setText("");
            view.getComplexIdField().setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Format invalid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void removeTaskFromComplexTask() {
        String complexTaskIdText = view.getComplexIdField().getText();
        String taskIdText = view.getComplexTaskIdToRemoveField().getText();

        if (complexTaskIdText.isEmpty() || taskIdText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Campurile Complex Task ID si ID to Remove nu trebuie sa fie goale", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int complexTaskId = Integer.parseInt(complexTaskIdText.trim());
            int taskIdToRemove = Integer.parseInt(taskIdText.trim());


            Task task1=this.taskMap.get(complexTaskId);
            Task taskToRemove=this.taskMap.get(taskIdToRemove);



            if (!(task1 instanceof ComplexTask)) {
                JOptionPane.showMessageDialog(view, "Nu exista acest Complex Task!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ((ComplexTask) task1).deleteTask(taskToRemove);

            DefaultTableModel complexTaskModel = (DefaultTableModel) view.getComplexTaskTable().getModel();
            for (int i = 0; i < complexTaskModel.getRowCount(); i++) {
                if ((int) complexTaskModel.getValueAt(i, 0) == complexTaskId) {
                    List<Integer> updatedIds = ((ComplexTask) (task1)).getListTasks().stream().map(Task::getIdTask).toList();
                    complexTaskModel.setValueAt(String.join(", ", updatedIds.stream().map(String::valueOf).toList()), i, 1);
                    complexTaskModel.setValueAt(((ComplexTask) (task1)).estimateDuration(), i, 2);
                    break;
                }
            }
            updateTaskTable();
            saveTasks();
            saveTaskManagement();
            view.getComplexTaskIdToRemoveField().setText("");
            view.getComplexTaskField().setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Format invalid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addTaskToComplexTask() {
        String complexTaskIdText = view.getComplexIdField().getText();
        String taskIdText = view.getComplexTaskIdToAddField().getText();

        if (complexTaskIdText.isEmpty() || taskIdText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Campurile Complex Task ID si ID to Add nu trebuie sa fie goale", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int complexTaskId = Integer.parseInt(complexTaskIdText.trim());
            int taskIdToAdd = Integer.parseInt(taskIdText.trim());

            Task task1=this.taskMap.get(complexTaskId);
            Task taskToAdd=this.taskMap.get(taskIdToAdd);


            if (!(task1 instanceof ComplexTask)) {
                JOptionPane.showMessageDialog(view, "Nu exista acest Complex Task!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ((ComplexTask) task1).addTask(taskToAdd);

            DefaultTableModel complexTaskModel = (DefaultTableModel) view.getComplexTaskTable().getModel();
            for (int i = 0; i < complexTaskModel.getRowCount(); i++) {
                if ((int) complexTaskModel.getValueAt(i, 0) == complexTaskId) {
                    List<Integer> updatedIds = ((ComplexTask) task1).getListTasks().stream().map(Task::getIdTask).toList();
                    complexTaskModel.setValueAt(String.join(", ", updatedIds.stream().map(String::valueOf).toList()), i, 1);
                    complexTaskModel.setValueAt(((ComplexTask) task1).estimateDuration(), i, 2);
                    break;
                }
            }
            updateTaskTable();
            saveTaskManagement();
            saveTasks();
            view.getComplexTaskIdToRemoveField().setText("");
            view.getComplexTaskField().setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Format Invalid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void assignTaskToEmployee() {

        try {
            String employeeID = view.getIdField().getText();
            String taskID = view.getSimpleTaskIdField().getText();
            if (employeeID.isEmpty() || taskID.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Campurile Employee ID si Task ID nu trebuie sa fie goale!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Employee employee = employeeMap.get(Integer.parseInt(employeeID));

            Task task=this.taskMap.get(Integer.parseInt(taskID));

            if (employee == null) {
                JOptionPane.showMessageDialog(view, "Employee nu a fost gasit!", "Error", JOptionPane.ERROR_MESSAGE);

                return;
            }
            if (task == null) {
                JOptionPane.showMessageDialog(view, "Task nu a fost gasit!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            employeeManagement.assignTaskToEmployee(employee,task);
            saveTasks();
            saveTaskManagement();
            updateTaskTable();
            updateEmployeeTable();
            JOptionPane.showMessageDialog(view, "Task atribuit cu succes!"+task.getStatusTask(), "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Eroare la atribuirea taskului: "+ e.getMessage() , "Error", JOptionPane.ERROR_MESSAGE);

        }
    }
    public void calculateEmployeeWork() {
        for (Employee emp : Employees) {
            int workTime = employeeManagement.calculateEmployeeWorkDuration(emp);
            emp.setWorkTime(workTime);

            DefaultTableModel employeeModel = (DefaultTableModel) view.getEmployeeTable().getModel();
            for (int i = 0; i < employeeModel.getRowCount(); i++) {
                if ((int) employeeModel.getValueAt(i, 0) == emp.getId()) {
                    employeeModel.setValueAt(workTime, i, 2);
                    break;
                }
            }
        }
        saveEmployees();
        saveTaskManagement();
    }



    public void modifyTaskStatus() {
        String taskIdText = view.getSimpleTaskIdField().getText();
        String newStatus = view.getSimpleTaskStatusField().getText();
        String employeeId=view.getIdField().getText();
        if (taskIdText.isEmpty() || newStatus.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Task ID si noul status nu trebuie să fie goale!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdText.trim());
            int emplId=Integer.parseInt(employeeId.trim());
            Task task=this.taskMap.get(taskId);

            Employee employee=employeeMap.get(emplId);
            employeeManagement.modifyTaskStatus(employeeMap.get(emplId),taskMap.get(taskId),newStatus);
            taskMap.get(taskId).setStatusTask(newStatus);

            if (task == null) {
                JOptionPane.showMessageDialog(view, "Task-ul nu a fost gasit!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            task.setStatusTask(newStatus);

            DefaultTableModel taskModel = (DefaultTableModel) view.getTaskTable().getModel();
            for (int i = 0; i < taskModel.getRowCount(); i++) {
                if ((int) taskModel.getValueAt(i, 0) == taskId) {
                    taskModel.setValueAt(newStatus, i, 1);
                    break;
                }
            }

            saveTasks();
            saveTaskManagement();
            JOptionPane.showMessageDialog(view, "Statusul task-ului a fost actualizat!", "Succes", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Task ID trebuie să fie un numar!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTaskTable() {
        DefaultTableModel model = (DefaultTableModel) view.getTaskTable().getModel();
        model.setRowCount(0);

        for (Task task : taskMap.values()) {
            model.addRow(new Object[]{
                    task.getIdTask(),
                    task.getStatusTask(),
                    task.estimateDuration()
            });
        }
    }
    private void updateEmployeeTable() {
        DefaultTableModel employeeModel = (DefaultTableModel) view.getEmployeeTable().getModel();
        employeeModel.setRowCount(0); // Șterge datele vechi din tabel

        for (Employee emp : this.Employees) {
            List<Task> assignedTasks = employeeManagement.getTaskMap().getOrDefault(emp, new ArrayList<>());
            String taskIds = assignedTasks.stream()
                    .map(task -> String.valueOf(task.getIdTask()))
                    .collect(Collectors.joining(", "));

            employeeModel.addRow(new Object[]{emp.getId(), emp.getName(), emp.getWorkTime(), taskIds});
        }
    }
    public void filterAndSortEmployees() {
        ArrayList<Employee> filteredList = new ArrayList<>(Utility.filterAndSortEmployees(employeeManagement));

        DefaultTableModel filteredModel = (DefaultTableModel) view.getFilteredEmployeeTable().getModel();
        filteredModel.setRowCount(0);

        for (Employee emp : filteredList) {
            filteredModel.addRow(new Object[]{emp.getId(), emp.getName(), emp.getWorkTime()});
        }

        JOptionPane.showMessageDialog(view, "Filtrarea si sortarea au fost realizate cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
    }
    public void countCompletedAndUncompletedTasks() {
        Map<String, Map<String, Integer>> taskCounts = Utility.countCompletedAndUncompletedTasks(employeeManagement);
        DefaultTableModel filteredModel = (DefaultTableModel) view.getFilteredEmployeeTable().getModel();
        filteredModel.setRowCount(0);
        for (Map.Entry<String, Map<String, Integer>> entry : taskCounts.entrySet()) {
            String employeeId = entry.getKey();
            Map<String, Integer> counts = entry.getValue();
            int completed = counts.getOrDefault("Completed", 0);
            int uncompleted = counts.getOrDefault("Uncompleted", 0);

            filteredModel.addRow(new Object[]{"-",employeeId,"-", "U: " + uncompleted + " | " +"C: " + completed});
        }
        JOptionPane.showMessageDialog(view, "Datele au fost actualizate în tabel!", "Succes", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveEmployees() {
        SerializationOperation.saveToFile(EMPLOYEE_FILE_NAME, Employees);
    }
    private void saveTasks(){
        SerializationOperation.saveToFile(TASK_FILE_NAME, Tasks);
    }
    private void saveTaskManagement() {
        SerializationOperation.saveToFile(TASK_MANAGEMENT_FILE, employeeManagement);
    }
    public void loadData() {
        Object taskManagementObj = SerializationOperation.loadFromFile(TASK_MANAGEMENT_FILE);
        if (taskManagementObj instanceof TaskManagement) {
            this.employeeManagement = (TaskManagement) taskManagementObj;
        }
        Object employeeObj = SerializationOperation.loadFromFile(EMPLOYEE_FILE_NAME);
        if (employeeObj instanceof List<?>) {
            List<?> tempList = (List<?>) employeeObj;
            if (!tempList.isEmpty() && tempList.get(0) instanceof Employee) {
                List<Employee> employeeData = (List<Employee>) tempList;

                DefaultTableModel employeeModel = (DefaultTableModel) view.getEmployeeTable().getModel();
                for (Employee emp : employeeData) {
                    List<Task> assignedTasks = employeeManagement.getTaskMap().getOrDefault(emp, new ArrayList<>());
                    String taskIds = assignedTasks.stream().map(task -> String.valueOf(task.getIdTask())).collect(Collectors.joining(", "));
                    employeeModel.addRow(new Object[]{emp.getId(), emp.getName(),emp.getWorkTime(),taskIds});
                    this.Employees.add(emp);
                    this.employeeManagement.addEmployee((emp));
                    employeeMap.put(emp.getId(),emp);
                }
            }
        }
        Object taskObj = SerializationOperation.loadFromFile(TASK_FILE_NAME);
        if (taskObj instanceof List<?>) {
            List<?> tempList = (List<?>) taskObj;
            if (!tempList.isEmpty() && tempList.get(0) instanceof Task) {
                List<Task> loadedTasks = (List<Task>) tempList;
                DefaultTableModel taskModel = (DefaultTableModel) view.getTaskTable().getModel();
                DefaultTableModel complexTaskModel = (DefaultTableModel) view.getComplexTaskTable().getModel();

                for (Task task : loadedTasks) {
                    this.Tasks.add(task);
                    this.employeeManagement.getTaskMapMang().put(task.getIdTask(),task);
                    this.taskMap.put(task.getIdTask(),task);
                    Task Task =task;
                    taskModel.addRow(new Object[]{Task.getIdTask(), Task.getStatusTask(), Task.estimateDuration()});
                    if (task instanceof ComplexTask) {
                        String taskIds = ((ComplexTask)(task)).getListTasks().stream().map(t -> String.valueOf(t.getIdTask())).collect(Collectors.joining(", "));
                        complexTaskModel.addRow(new Object[]{task.getIdTask(),taskIds, task.estimateDuration()});
                    }
                }
            }
        }
    }
}