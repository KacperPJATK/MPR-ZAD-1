package service;

import model.Employee;
import repository.EmployeesRepository;

public class EmployeesManagerImpl implements EmployeesManager {

    private final EmployeesRepository repository;

    public EmployeesManagerImpl(EmployeesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addEmployee(Employee employee) {
        repository.add(employee);
    }

    @Override
    public void displayEmployees() {
        repository.getEmployees().forEach(System.out::println);
    }
}
