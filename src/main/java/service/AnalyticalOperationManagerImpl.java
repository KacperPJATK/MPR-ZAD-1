package service;

import model.Employee;
import model.Position;
import repository.EmployeesRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticalOperationManagerImpl implements AnalyticalOperationManager {

    private final EmployeesRepository repository;

    public AnalyticalOperationManagerImpl(EmployeesRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> findEmployeesByCompany(String companyName) {
        return repository.getEmployees().stream()
                .filter(employee -> companyName.equals(employee.getCompanyName()))
                .toList();
    }

    @Override
    public List<Employee> getEmployeesAlphabetically() {
        return repository.getEmployees().stream()
                .sorted(Comparator.comparing(Employee::getSurname))
                .toList();
    }

    @Override
    public Map<Position, List<Employee>> getEmployeesGroupedByPosition() {
        return repository.getEmployees()
                .stream()
                .collect(Collectors.groupingBy(
                        Employee::getPosition
                ));
    }

    @Override
    public Map<Position, Long> getNumberOfEmployeesPerPosition() {
        return repository.getEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employee::getPosition,
                        Collectors.counting()
                ));
    }
}
