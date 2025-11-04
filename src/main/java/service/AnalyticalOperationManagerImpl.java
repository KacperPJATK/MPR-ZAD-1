package service;

import model.CompanyStatistics;
import model.Employee;
import model.Position;
import repository.EmployeesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticalOperationManagerImpl implements AnalyticalOperationManager {


    @Override
    public List<Employee> findEmployeesByCompany(String companyName) {
        return EmployeesRepository.getEmployees().stream()
                .filter(employee -> companyName.equals(employee.getCompanyName()))
                .toList();
    }

    @Override
    public List<Employee> getEmployeesAlphabetically() {
        return EmployeesRepository.getEmployees().stream()
                .sorted(Comparator.comparing(Employee::getSurname))
                .toList();
    }

    @Override
    public Map<Position, List<Employee>> getEmployeesGroupedByPosition() {
        return EmployeesRepository.getEmployees()
                .stream()
                .collect(Collectors.groupingBy(
                        Employee::getPosition
                ));
    }

    @Override
    public Map<Position, Long> getNumberOfEmployeesPerPosition() {
        return EmployeesRepository.getEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employee::getPosition,
                        Collectors.counting()
                ));
    }

    @Override
    public Map<String, CompanyStatistics> getCompanyStatistics(String companyName) {
        List<Employee> employees = EmployeesRepository.getEmployees();
        List<Employee> employeesPerCompany = employees.stream()
                .filter(employee -> companyName.equals(employee.getCompanyName()))
                .toList();

        if (employeesPerCompany.isEmpty()) {
            return Collections.emptyMap();
        }


        Map<String, CompanyStatistics> result = new HashMap<>();

        result.put(companyName, new CompanyStatistics(
                employeesPerCompany.size(),
                getAverageSalaryForCompany(employees),
                getBestPaidEmployeeInCompanyCredentials(employees)
        ));

        return result;

    }

    private BigDecimal getAverageSalaryForCompany(List<Employee> employees) {
        return employees.stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(
                        BigDecimal.valueOf(employees.size()),
                        RoundingMode.HALF_UP
                );
    }

    private String getBestPaidEmployeeInCompanyCredentials(List<Employee> employees) {
        Optional<Employee> bestPaidEmployee = employees.stream()
                .max(Comparator.comparing(Employee::getSalary));

        if (bestPaidEmployee.isPresent()) {
            String name = bestPaidEmployee.get().getName();
            String surname = bestPaidEmployee.get().getSurname();
            return String.format("%s %s", name, surname);
        }

        return "";

    }

}
