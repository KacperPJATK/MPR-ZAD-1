package service;

import model.Employee;
import repository.EmployeesRepository;

import java.util.List;

public class EmployeesManagerImpl implements EmployeesManager {


    @Override
    public boolean addEmployee(Employee employee) {
        return EmployeesRepository.add(employee);
    }

    @Override
    public boolean displayEmployees() {
        List<Employee> employees = EmployeesRepository.getEmployees();
        if (employees.isEmpty()) {
            return false;
        } else {
            employees.forEach(System.out::println);
            return true;
        }
    }
}
