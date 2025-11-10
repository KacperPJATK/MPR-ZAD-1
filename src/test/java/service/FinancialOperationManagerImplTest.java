package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.EmployeesRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FinancialOperationManagerImplTest {

    private FinancialOperationManager financialOperationManager;


    @AfterAll
    static void cleanRepository() {
        EmployeesRepository.clearForTest();
    }

    private static void addTestEmployees() {
        EmployeesRepository.add(new Employee(
                "Leon", "Kennedy",
                "kennedy@protonmail.com", "Capcom", Position.PREZES
        ));
        EmployeesRepository.add(new Employee(
                "Chris", "Redfield",
                "redfield@protonmail.com", "Capcom", Position.WICEPREZES
        ));
    }

    @BeforeEach
    void setUp() {
        EmployeesRepository.clearForTest();
        financialOperationManager = new FinancialOperationManagerImpl();
    }

    @Test
    void getAverageSalary() {
//        given
        addTestEmployees();
        BigDecimal expected = new BigDecimal("21500.00");
//        when
        BigDecimal result = financialOperationManager.getAverageSalary();
//        then
        Assertions.assertEquals(expected, result);

    }

    @Test
    void getAverageSalaryWhenThereAreNoEmployees() {
//        given
        BigDecimal expected = BigDecimal.ZERO;
//        when
        BigDecimal result = financialOperationManager.getAverageSalary();
//        then
        Assertions.assertEquals(expected, result);
    }


    @Test
    void getTheBestPaidEmployee() {
//        given
        addTestEmployees();
        Employee expected = new Employee(
                "Leon", "Kennedy",
                "kennedy@protonmail.com", "Capcom", Position.PREZES
        );
//        when
        Employee result = financialOperationManager.getTheBestPaidEmployee();
//        then
        Assertions.assertEquals(expected, result);
    }

    @Test
    void getTheBestPaidEmployeeWhenThereAreNoEmployees() {
//        given,when
        Employee result = financialOperationManager.getTheBestPaidEmployee();
//        then
        Assertions.assertNull(result);
    }

    @Test
    void validateSalaryConsistency() {
//        given
        addTestEmployees();
        List<Employee> expected = new ArrayList<>();
//        when
        List<Employee> result = financialOperationManager.validateSalaryConsistency();
//        then
        Assertions.assertEquals(expected, result);
    }

    @Test
    void validateSalaryConsistencyWhenThereAreNoEmployees() {
//        given
        List<Employee> expected = Collections.emptyList();
//        when
        List<Employee> result = financialOperationManager.validateSalaryConsistency();
//        then
        Assertions.assertEquals(expected, result);
    }


}