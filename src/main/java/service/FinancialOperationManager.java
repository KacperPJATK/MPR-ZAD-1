package service;

import model.Employee;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialOperationManager {
    BigDecimal getAverageSalary();

    Employee getTheBestPaidEmployee();

    List<Employee> validateSalaryConsistency();

}
