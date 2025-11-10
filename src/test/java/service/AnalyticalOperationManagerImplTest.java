package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.*;
import repository.EmployeesRepository;

import java.math.BigDecimal;
import java.util.*;

class AnalyticalOperationManagerImplTest {

    private AnalyticalOperationManager analyticalOperationManager;

    private List<Employee> testList;

    @BeforeAll
    static void prepareRepository() {
        EmployeesRepository.clearForTest();
        Employee employee1 = new Employee(
                "Leon", "Kennedy",
                "kennedy@protonmail.com", "Capcom", Position.PREZES
        );
        Employee employee2 = new Employee(
                "Chris", "Redfield",
                "redfield@protonmail.com", "Capcom", Position.WICEPREZES
        );
        EmployeesRepository.add(employee1);
        EmployeesRepository.add(employee2);
    }

    @AfterAll
    static void cleanRepository() {
        EmployeesRepository.clearForTest();
    }

    @BeforeEach
    void setUp() {
        analyticalOperationManager = new AnalyticalOperationManagerImpl();
        testList = new ArrayList<>();
        testList.add(new Employee(
                "Leon", "Kennedy",
                "kennedy@protonmail.com", "Capcom", Position.PREZES
        ));
        testList.add(new Employee(
                "Chris", "Redfield",
                "redfield@protonmail.com", "Capcom", Position.WICEPREZES
        ));

    }

    @Test
    void findEmployeesByCompany() {
//        given
        String companyName = "Capcom";
//        when
        List<Employee> result = analyticalOperationManager.findEmployeesByCompany(companyName);
//        then
        Assertions.assertAll(
                () -> Assertions.assertEquals(testList.size(), result.size()),
                () -> Assertions.assertTrue(testList.containsAll(result))
        );

    }

    @Test
    void getEmployeesAlphabetically() {
//        given
        testList.sort(Comparator.comparing(Employee::getSurname));
//        when
        List<Employee> result = analyticalOperationManager.getEmployeesAlphabetically();
        Assertions.assertAll(
                () -> Assertions.assertEquals(testList.size(), result.size()),
                () -> Assertions.assertEquals(testList, result)
        );
    }

    @Test
    void getEmployeesGroupedByPosition() {
//        given
        Map<Position, List<Employee>> expected = new HashMap<>();
        expected.put(Position.PREZES, List.of(testList.getFirst()));
        expected.put(Position.WICEPREZES, List.of(testList.getLast()));
//        when
        var result = analyticalOperationManager.getEmployeesGroupedByPosition();
//        then
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.size(), result.size()),
                () -> Assertions.assertEquals(expected, result),
                () -> Assertions.assertEquals(expected.keySet(), result.keySet())
        );
    }

    @Test
    void getNumberOfEmployeesPerPosition() {
//        given
        Map<Position, Long> expected = new HashMap<>();
        expected.put(Position.PREZES, 1L);
        expected.put(Position.WICEPREZES, 1L);
//        when
        var result = analyticalOperationManager.getNumberOfEmployeesPerPosition();
//        then
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.size(), result.size()),
                () -> Assertions.assertEquals(expected, result),
                () -> Assertions.assertEquals(expected.keySet(), result.keySet())
        );
    }

    @Test
    void getCompanyStatistics() {
//        given
        String companyName = "Capcom";
        var expectedSalary = BigDecimal.valueOf(21500);
        String expectedCredentials = "Leon Kennedy";

//        when
        var result = analyticalOperationManager.getCompanyStatistics(companyName);
//        then
        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        expectedSalary, result.get(companyName).getAverageSalary()
                ),
                () -> Assertions.assertEquals(
                        2, result.get(companyName).getNumberOfEmployeesInCompany()
                ),
                () -> Assertions.assertEquals(
                        expectedCredentials, result.get(companyName).getBestEarningEmployeeCredentials()
                ),
                () -> Assertions.assertTrue(result.containsKey(companyName))
        );
    }

    @Test
    void getCompanyStatisticsWhenThereAreNoEmployees() {
//        given
        String companyName = "Naughty Dog";
//        when
        var result = analyticalOperationManager.getCompanyStatistics(companyName);
//        then
        Assertions.assertEquals(Collections.emptyMap(), result);
    }
}