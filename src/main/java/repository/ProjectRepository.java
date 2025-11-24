package repository;

import model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectRepository {

    private static final Map<String, List<Employee>> PROJECTS = new HashMap<>();


    public static void addProject(String teamName, List<Employee> assignedToProject) {
        PROJECTS.put(teamName, assignedToProject);
    }

    public static void moveBetween(String email, String from, String to) {
        boolean remove = PROJECTS.get(from).remove(EmployeesRepository.getEmployee(email));
        if (remove) {
            PROJECTS.get(to).add(EmployeesRepository.getEmployee(email));
        }
    }

    public static boolean containsTeam(String teamName) {
        return PROJECTS.containsKey(teamName);
    }

    public static void clearForTest() {
        PROJECTS.clear();
    }

    public static List<Employee> getTeam(String teamName) {
        return PROJECTS.get(teamName);
    }
}

