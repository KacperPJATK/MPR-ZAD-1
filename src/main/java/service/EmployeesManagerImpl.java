package service;

import model.Employee;
import repository.EmployeesRepository;

public class EmployeesManagerImpl implements EmployeesManager {


    @Override
    public void addEmployee(Employee employee) {
        EmployeesRepository.add(employee);
    }

    @Override
    public void displayEmployees() {
        EmployeesRepository.getEmployees().forEach(System.out::println);
    }
}
