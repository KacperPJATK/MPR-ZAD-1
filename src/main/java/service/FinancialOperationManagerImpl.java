package service;

import model.Employee;
import repository.EmployeesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

public class FinancialOperationManagerImpl implements FinancialOperationManager {

    private final EmployeesRepository repository;

    public FinancialOperationManagerImpl(EmployeesRepository repository) {
        this.repository = repository;
    }

    @Override
    public BigDecimal getAverageSalary() {
        List<Employee> employees = repository.getEmployees();

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
        List<Employee> employees = repository.getEmployees();
        return employees.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .orElse(null);
    }
}
