package service;

import model.Employee;

import java.util.List;

public interface EmployeeFormatter {
    String format(List<Employee> employees, String format);
}
