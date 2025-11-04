package service;

import model.CompanyStatistics;
import model.Employee;
import model.Position;

import java.util.List;
import java.util.Map;

public interface AnalyticalOperationManager {
    List<Employee> findEmployeesByCompany(String companyName);

    List<Employee> getEmployeesAlphabetically();

    Map<Position, List<Employee>> getEmployeesGroupedByPosition();

    Map<Position, Long> getNumberOfEmployeesPerPosition();

    Map<String, CompanyStatistics> getCompanyStatistics(String companyName);
}
