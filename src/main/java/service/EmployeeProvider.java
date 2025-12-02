package service;

import model.Employee;

import java.util.List;

public interface EmployeeProvider {
    List<Employee> findAll();
}
