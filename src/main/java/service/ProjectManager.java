package service;

import model.Employee;

import java.util.List;

public interface ProjectManager {
    boolean createTeam(String teamName, List<Employee> assignedToProject);

    boolean moveEmployee(String email, String from, String to);
}
