package repository;

import model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeesRepository {
    private static final Map<String, Employee> employees = new HashMap<>();

    public static boolean add(Employee employee) {
        if (employees.containsKey(employee.getEmail())) {
            throw new IllegalArgumentException("Pracownik o takim mailu już istnieje");
        } else {
            employees.put(employee.getEmail(), employee);
            return true;
        }
    }

    public static List<Employee> getEmployees() {
        return List.copyOf(employees.values());
    }

    public static void clearForTest() {
        employees.clear();
    }

    public static boolean containsEmail(String email) {
        return employees.containsKey(email.toLowerCase());
    }

    public static Employee getEmployee(String email) {
        return employees.get(email);
    }
}
