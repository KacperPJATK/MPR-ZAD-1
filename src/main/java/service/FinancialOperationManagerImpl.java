package service;

import model.Employee;
import repository.EmployeesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FinancialOperationManagerImpl implements FinancialOperationManager {


    @Override
    public BigDecimal getAverageSalary() {
        List<Employee> employees = EmployeesRepository.getEmployees();

        if (employees.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            return employees.stream()
                    .map(Employee::getSalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(
                            BigDecimal.valueOf(employees.size()),
                            2,
                            RoundingMode.HALF_UP
                    );
        }
    }

    @Override
    public Employee getTheBestPaidEmployee() {
        List<Employee> employees = EmployeesRepository.getEmployees();
        return employees.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .orElse(null);
    }

    @Override
    public List<Employee> validateSalaryConsistency() {
        List<Employee> employees = EmployeesRepository.getEmployees();
        if (employees.isEmpty()) {
            return Collections.emptyList();
        } else {
            return employees.stream()
                    .filter(employee -> employee.getSalary().compareTo(employee.getPosition().getSalary()) < 0)
                    .toList();

        }
    }
}
