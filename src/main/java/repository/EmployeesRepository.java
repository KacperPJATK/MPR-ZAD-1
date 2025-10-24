package repository;

import model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeesRepository {
    private final Map<String, Employee> employees = new HashMap<>();

    public List<Employee> getEmployees() {
        return List.copyOf(employees.values());
    }

    public void add(Employee employee) {
        if (employees.containsKey(employee.getEmail())) {
            throw new IllegalArgumentException("Pracownik o takim mailu już istnieje");
        } else {
            employees.put(employee.getEmail(), employee);
        }
    }
}
