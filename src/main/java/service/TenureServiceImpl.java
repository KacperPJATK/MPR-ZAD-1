package service;

import model.DemographicReport;
import model.Employee;
import repository.EmployeesRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class TenureServiceImpl implements TenureService {
    @Override
    public long calculateTenure(String email) {
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("You can pass null as an email");
        }

        if (!EmployeesRepository.containsEmail(email)) {
            String message = String.format("There is no such employee with email: %s", email);
            throw new NoSuchElementException(message);
        }
        LocalDate empDate = EmployeesRepository.getEmployee(email).getEmploymentDate();
        return Period.between(empDate, LocalDate.now()).getYears();
    }

    @Override
    public List<Employee> getAnniversaries() {
        List<Employee> employees = EmployeesRepository.getEmployees();
        if (employees.isEmpty()) {
            return Collections.emptyList();
        }
        return employees.stream()
                .filter(employee -> calculateTenure(employee.getEmail()) % 5 == 0)
                .toList();

    }

    @Override
    public List<Employee> getEmployeesWithTenureLength(int tenureLength) {
        List<Employee> employees = EmployeesRepository.getEmployees();
        if (employees.isEmpty()) {
            return Collections.emptyList();
        }
        return employees.stream()
                .filter(employee -> calculateTenure(employee.getEmail()) >= tenureLength)
                .toList();
    }

    @Override
    public DemographicReport getDemographicReport(String companyName) {
        List<Employee> employees = EmployeesRepository.getEmployees().stream()
                .filter(employee -> companyName.equals(employee.getCompanyName()))
                .toList();
        if (employees.isEmpty()) {
            String message = String.format("There are no employees for company: %s", companyName);
            throw new NoSuchElementException(message);
        }
        Map<Long, List<Employee>> tenureMap = employees.stream()
                .collect(Collectors.groupingBy(
                        employee -> calculateTenure(employee.getEmail())
                ));
        return new DemographicReport(companyName, tenureMap);
    }
}