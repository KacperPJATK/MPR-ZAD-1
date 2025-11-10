package service;

import model.Employee;

public interface EmployeesManager {
    boolean addEmployee(Employee employee);

    boolean displayEmployees();
}
