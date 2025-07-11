package org.example.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class View extends JFrame {
    Controller controller = new Controller(this);
    private JPanel mainPanel;

    private JPanel employeePanel;
    private JLabel employeeeName;
    private JLabel employeeeId;
    private JTextField nameField;
    private JTextField idField;

    private JPanel taskPanel;
    private JLabel simpleTaskStatus;
    private JLabel simpleTaskId;
    private JLabel startHour;
    private JLabel endHour;
    private JTextField simpleTaskStatusField;
    private JTextField simpleTaskIdField;
    private JTextField startHourField;
    private JTextField endHourField;

    private JPanel complexTaskPanel;
    private JLabel complexTaskLabel;
    private JLabel complexTaskIdLabel;
    private JLabel removeTaskLabel;
    private JLabel addTaskLabel;
    private JTextField complexTaskField;
    private JTextField complexTaskIdField;
    private JTextField removeTaskField;
    private JTextField addTaskField;



    private JPanel buttonPanel;
    private JButton removeTaskButton;
    private JButton addTaskButton;
    private JButton addEmployee;
    private JButton assignTaskToEmployee ;
    private JButton calculateEmployeeWorkDuration;
    private JButton  modifyTaskStatus;
    private JButton createComplexTask;
    private JButton addTaskToComplex;
    private JButton filterAndSortButton;
    private JButton showCompletedTaskButton;


    private JPanel dataPanel;

    private JTable employeeTable;
    private JScrollPane employeeScrollPane;
    private String[] colEmployee;

    private JTable taskTable;
    private JScrollPane taskScrollPane;
    private String[] colTask;

    private JTable complexTaskTable;
    private JScrollPane complexTaskScrollPane;
    private String[] colComplexTask;

    private JTable filteredEmployeeTable;
    private JScrollPane filteredEmployeeScrollPane;
    private String[] colFilteredEmployee;

    public View(){
        prepareGUI();
        controller.loadData();
    }
    public void prepareGUI(){
        this.setSize(800, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.mainPanel = new JPanel(new GridLayout(5, 5));
        prepareEmployeeData();
        prepareTaskData();
        prepareComplexTaskData();
        prepareButtons();
        prepareData();
        this.setContentPane(this.mainPanel);
    }
    public void prepareEmployeeData(){
        this.employeePanel = new JPanel(new GridLayout(2, 2));
        this.employeeeName=new JLabel("Employee Name:");
        this.employeeeId=new JLabel("Employee ID:");
        this.nameField=new JTextField();
        this.idField=new JTextField();
        this.employeePanel.add(employeeeName);
        this.employeePanel.add(nameField);
        this.employeePanel.add(employeeeId);
        this.employeePanel.add(idField);
        this.mainPanel.add(employeePanel);
    }
    public void prepareTaskData(){
        this.taskPanel=new JPanel(new GridLayout(4,2));
        this.simpleTaskStatus=new JLabel("Task Status:");
        this.simpleTaskId=new JLabel("Task ID:");
        this.startHour=new JLabel("Start Hour:");
        this.endHour=new JLabel("End Hour:");
        this.simpleTaskStatusField=new JTextField();
        this.simpleTaskIdField=new JTextField();
        this.startHourField=new JTextField();
        this.endHourField=new JTextField();

        this.taskPanel.add(simpleTaskStatus);
        this.taskPanel.add(simpleTaskStatusField);

        this.taskPanel.add(simpleTaskId);
        this.taskPanel.add(simpleTaskIdField);

        this.taskPanel.add(startHour);
        this.taskPanel.add(startHourField);

        this.taskPanel.add(endHour);
        this.taskPanel.add(endHourField);

        this.mainPanel.add(this.taskPanel);
    }
    public void prepareComplexTaskData(){
        this.complexTaskPanel = new JPanel(new GridLayout(4, 1));
        this.complexTaskLabel = new JLabel("Enter Tasks IDs (comma separated):");
        this.complexTaskIdLabel = new JLabel("Complex Task ID:");
        this.complexTaskField = new JTextField();
        this.complexTaskIdField = new JTextField();
        this.removeTaskLabel = new JLabel("ID to Remove:");
        this.removeTaskField = new JTextField();
        this.addTaskLabel = new JLabel("ID to Add:");
        this.addTaskField = new JTextField();

        complexTaskPanel.add(complexTaskIdLabel);
        complexTaskPanel.add(complexTaskIdField);

        complexTaskPanel.add(complexTaskLabel);
        complexTaskPanel.add(complexTaskField);

        complexTaskPanel.add(removeTaskLabel);
        complexTaskPanel.add(removeTaskField);

        complexTaskPanel.add(addTaskLabel);
        complexTaskPanel.add(addTaskField);
        mainPanel.add(complexTaskPanel);

    }
    public void prepareButtons(){
        this.buttonPanel=new JPanel(new GridLayout(2,5));

        this.addEmployee=new JButton("Add Employee");
        this.addEmployee.setActionCommand("addEmployee");
        this.addEmployee.addActionListener(this.controller);

        this.assignTaskToEmployee=new JButton("Assign Task To an Employee");
        this.assignTaskToEmployee.setActionCommand("assignTask");
        this.assignTaskToEmployee.addActionListener(this.controller);

        this.calculateEmployeeWorkDuration=new JButton("Calculate Work Duration of an Employee");
        this.calculateEmployeeWorkDuration.setActionCommand("EmployeeWorkDuration");
        this.calculateEmployeeWorkDuration.addActionListener(this.controller);

        this.modifyTaskStatus=new JButton("Modify Task Status");
        this.modifyTaskStatus.setActionCommand("modifyTaskStatus");
        this.modifyTaskStatus.addActionListener(this.controller);

        this.addTaskButton = new JButton("Create Task");
        this.addTaskButton.setActionCommand("addTask");
        this.addTaskButton.addActionListener(this.controller);

        this.createComplexTask = new JButton("Create Complex Task");  // Nou buton
        this.createComplexTask.setActionCommand("createComplexTask");
        this.createComplexTask.addActionListener(this.controller);

        this.removeTaskButton = new JButton("Remove Task From Complex");
        this.removeTaskButton.setActionCommand("removeTask");
        this.removeTaskButton.addActionListener(this.controller);

        this.addTaskToComplex = new JButton("Add Task to Complex");
        this.addTaskToComplex.setActionCommand("addTaskToComplex");
        this.addTaskToComplex.addActionListener(this.controller);

        this.filterAndSortButton = new JButton("Filter & Sort Employees");
        this.filterAndSortButton.setActionCommand("filterAndSort");
        this.filterAndSortButton.addActionListener(this.controller);

        this.showCompletedTaskButton = new JButton("Completed Tasks");
        this.showCompletedTaskButton.setActionCommand("completedTasks");
        this.showCompletedTaskButton.addActionListener(this.controller);

        this.buttonPanel.add(this.addEmployee);
        this.buttonPanel.add(this.assignTaskToEmployee);
        this.buttonPanel.add(this.calculateEmployeeWorkDuration);
        this.buttonPanel.add(this.modifyTaskStatus);
        this.buttonPanel.add(this.addTaskButton);
        this.buttonPanel.add(this.createComplexTask);
        this.buttonPanel.add(this.removeTaskButton);
        this.buttonPanel.add(this.addTaskToComplex);
        this.buttonPanel.add(this.filterAndSortButton);
        this.buttonPanel.add(this.showCompletedTaskButton);
        this.mainPanel.add(this.buttonPanel);
    }
    public void prepareData(){
        this.dataPanel = new JPanel(new GridLayout(1, 3));

        this.colEmployee = new String[]{"Employee ID", "Employee Name","Total Work Hours","Tasks"};
        DefaultTableModel employeeModel = new DefaultTableModel(this.colEmployee, 0); // Inițial fără date
        this.employeeTable = new JTable(employeeModel);
        this.employeeScrollPane = new JScrollPane(this.employeeTable);


        this.colTask = new String[]{"Task ID", "Task Name","Estimate Duration"};
        DefaultTableModel taskModel = new DefaultTableModel(this.colTask, 0);
        this.taskTable = new JTable(taskModel);
        this.taskScrollPane = new JScrollPane(this.taskTable);

        this.colComplexTask = new String[]{"Complex Task ID", "IDs Taks", "Total Duration"}; // Nou tabel
        DefaultTableModel complexTaskModel = new DefaultTableModel(this.colComplexTask, 0);
        this.complexTaskTable = new JTable(complexTaskModel);
        this.complexTaskScrollPane = new JScrollPane(this.complexTaskTable);

        this.colFilteredEmployee = new String[]{"ID", "Employee Name", "Work Hours","Completed Tasks"};
        DefaultTableModel filteredEmployeeModel = new DefaultTableModel(this.colFilteredEmployee, 0);
        this.filteredEmployeeTable = new JTable(filteredEmployeeModel);
        this.filteredEmployeeScrollPane = new JScrollPane(this.filteredEmployeeTable);

        this.dataPanel.add(this.employeeScrollPane);
        this.dataPanel.add(this.taskScrollPane);
        this.dataPanel.add(this.complexTaskScrollPane);
        this.dataPanel.add(this.filteredEmployeeScrollPane);

        this.mainPanel.add(this.dataPanel);
    }
    public JTextField getNameField() {return this.nameField;}
    public JTextField getIdField() {return this.idField;}
    public JTable getEmployeeTable() {return employeeTable;}


    public JTextField getSimpleTaskIdField() { return simpleTaskIdField; }
    public JTextField getSimpleTaskStatusField() { return simpleTaskStatusField; }
    public JTextField getStartHourField() { return startHourField; }
    public JTextField getEndHourField() { return endHourField; }
    public JTable getTaskTable() {return taskTable;}

    public JTextField getComplexTaskField() {return complexTaskField;}
    public JTextField getComplexIdField() {return complexTaskIdField;}
    public JTable getComplexTaskTable() {return complexTaskTable;}
    public JTextField getComplexTaskIdToRemoveField() {return this.removeTaskField;}
    public JTextField getComplexTaskIdToAddField() {return this.addTaskField;}

    public JTable getFilteredEmployeeTable() {return filteredEmployeeTable;}
}