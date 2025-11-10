package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.EmployeesRepository;

class EmployeesManagerImplTest {


    private EmployeesManager employeesManager;

    @BeforeEach
    void setUp() {
        EmployeesRepository.clearForTest();
        employeesManager = new EmployeesManagerImpl();
    }


    @Test
    void addEmployee() {
//        given
        Employee employee = new Employee(
                "Sam", "Miller",
                "miller@protonmail.com", "Naughty Dog", Position.PROGRAMISTA
        );
//        when
        boolean result = employeesManager.addEmployee(employee);

//        then
        Assertions.assertTrue(result);

    }

    @Test
    void shouldFailWhenAddingEmployeeWithTheSameEmail() {
        //        given
        Employee employee = new Employee(
                "Sam", "Drake",
                "drake@protonmail.com", "Naughty Dog", Position.PROGRAMISTA
        );
//        when
        employeesManager.addEmployee(employee);
//        then
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> employeesManager.addEmployee(employee)
        );
    }

    @Test
    void displayEmployees() {
//        given
        Employee employee = new Employee(
                "Sam", "Drake",
                "drake@protonmail.com", "Naughty Dog", Position.PROGRAMISTA
        );
        employeesManager.addEmployee(employee);
//        when
        boolean result = employeesManager.displayEmployees();
//        then
        Assertions.assertTrue(result);
    }

    @Test
    void ShouldReturnFalseIfThereIsNothingToDisplay() {
//        given, when
        boolean result = employeesManager.displayEmployees();
//        then
        Assertions.assertFalse(result);


    }


}