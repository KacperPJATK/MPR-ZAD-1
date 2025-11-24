package service;

import model.DemographicReport;
import model.Employee;

import java.util.List;

public interface TenureService {
    long calculateTenure(String email);

    List<Employee> getAnniversaries();

    List<Employee> getEmployeesWithTenureLength(int tenureLength);

    DemographicReport getDemographicReport(String companyName);
}
