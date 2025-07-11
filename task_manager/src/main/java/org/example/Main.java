package org.example;

import org.example.gui.Controller;
import org.example.gui.View;
import org.example.logic.ComplexTask;
import org.example.logic.Employee;
import org.example.logic.SimpleTask;
import org.example.logic.TaskManagement;

import javax.swing.*;
import java.util.Map;

import static java.lang.Thread.sleep;
import static org.example.logic.Utility.countCompletedAndUncompletedTasks;
import static org.example.logic.Utility.filterAndSortEmployees;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new View();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);
    }
}