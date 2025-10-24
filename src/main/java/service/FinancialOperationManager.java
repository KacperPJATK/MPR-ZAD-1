package service;

import model.Employee;

import java.math.BigDecimal;

public interface FinancialOperationManager {
    BigDecimal getAverageSalary();

    Employee getTheBestPaidEmployee();
}
