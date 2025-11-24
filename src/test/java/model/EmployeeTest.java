package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import repository.EmployeesRepository;

import java.time.LocalDate;
import java.util.stream.Stream;

class EmployeeTest {

    private Employee employee;

    public static Stream<Arguments> testHashCodeData() {
        return Stream.of(
                Arguments.of(
                        "Aiden", "Pierce",
                        "pierce@gmial.com", "TechCorp", Position.PROGRAMISTA
                ),
                Arguments.of(
                        "Aiden", "Pierce",
                        "pierce@gmial.com", "TechCorp", Position.PREZES
                ),
                Arguments.of(
                        "Aiden", "Pierce1",
                        "pierce@gmial.com", "TechCorp", Position.PROGRAMISTA
                ),
                Arguments.of(
                        "Aiden1", "Pierce",
                        "pierce@gmial.com", "TechCorp", Position.PROGRAMISTA
                ),
                Arguments.of(
                        "Aiden", "Pierce",
                        "pierce@gmial.com", "TechCrap", Position.PROGRAMISTA
                )
        );
    }

    public static Stream<Arguments> equalsData() {
        return Stream.of(
                Arguments.of(
                        "Jeff", "Pierce",
                        "pierce@gmial.com", "TechCorp", Position.PROGRAMISTA
                ),
                Arguments.of(
                        "Aiden", "Miller",
                        "pierce@gmial.com", "TechCorp", Position.PROGRAMISTA
                ),
                Arguments.of(
                        "Aiden", "Pierce",
                        "pierce@gmial.com", "TechCorp", Position.PROGRAMISTA
                ),
                Arguments.of(
                        "Aiden", "Pierce",
                        "pierce@gmial.com", "TechCrap", Position.PROGRAMISTA
                ),
                Arguments.of(
                        "Aiden", "Pierce",
                        "pierce@gmial.com", "TechCorp", Position.PREZES
                )
        );
    }

    public static Stream<Arguments> provideIllegalEmail() {
        return Stream.of(
                null,
                Arguments.of("")
        );
    }

    @BeforeEach
    void setUp() {
        employee = new Employee(
                "Aiden", "Pierce",
                "pierce@gmial.com", "TechCorp", Position.PROGRAMISTA,
                LocalDate.now()
        );
        EmployeesRepository.add(employee);
    }

    @AfterEach
    void clearRepository() {
        EmployeesRepository.clearForTest();
    }

    @MethodSource("testHashCodeData")
    @ParameterizedTest
    void testHashCode(String name, String surname, String email, String companyName, Position position) {
//        given
        Employee testEmployee = new Employee(name, surname, email, companyName, position, LocalDate.now());
//        when
        int result = employee.hashCode();
        int expected = testEmployee.hashCode();
//        then
        Assertions.assertEquals(expected, result);
    }

    @Test
    void hashCodeShouldFailWhenComparingEmployeesWithDifferentEmails() {
//        given
        Employee testEmployee = new Employee(
                "Aiden", "Pierce",
                "miller@gmial.com", "TechCorp", Position.PROGRAMISTA,
                LocalDate.now()
        );
//        when
        int expected = employee.hashCode();
        int result = testEmployee.hashCode();
//        then
        Assertions.assertNotEquals(expected, result);
    }

    @Test
    void equalsShouldFailWhenComparingDifferentEmployees() {
//        given
        Employee testEmployee = new Employee(
                "Aiden", "Pierce",
                "miller@gmial.com", "TechCorp", Position.PROGRAMISTA,
                LocalDate.now()
        );
//        when
        boolean result = employee.equals(testEmployee);
//        then
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @MethodSource("equalsData")
    void testEquals(String name, String surname, String email, String companyName, Position position) {
//        given
        Employee testEmployee = new Employee(name, surname, email, companyName, position, LocalDate.now());
//        when
        boolean result = employee.equals(testEmployee);
//        then
        Assertions.assertTrue(result);
    }

    @Test
    void emptyEmployee() {
//        given
        Employee expected = new Employee("empty", "empty");
//        when
        Employee result = Employee.emptyEmployee();
//        then
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getName(), result.getName()),
                () -> Assertions.assertEquals(expected.getSurname(), result.getSurname())
        );

    }

    @Test
    void setPositionNullShouldThrow() {
//        given, when, then
        Assertions.assertThrows(NullPointerException.class, () -> employee.setPosition(null));
    }

    @Test
    void setPosition() {
//        given
        Position position = Position.PREZES;
//        when
        employee.setPosition(position);
//        then
        Assertions.assertAll(
                () -> Assertions.assertEquals(position, employee.getPosition()),
                () -> Assertions.assertEquals(position.getHierarchyLevel(), employee.getPosition().getHierarchyLevel()),
                () -> Assertions.assertEquals(position.getSalary(), employee.getPosition().getSalary())
        );
    }

    @Test
    void setEmail() {
//        given
        String email = "miller@gmial.com";
//        when
        employee.setEmail(email);
//        then
        Assertions.assertEquals(email, employee.getEmail());
    }

    @Test
    void changingToAlreadyExistingEmailShouldThrow() {
//        given
        String email = "pierce@gmial.com";
//        when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> employee.setEmail(email));
    }

    @ParameterizedTest
    @MethodSource("provideIllegalEmail")
    void creatingEmployeeWithNullEmailShouldFail(String email) {
//        given, when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Employee(
                "Aiden", "Pierce",
                email, "TechCorp", Position.PROGRAMISTA,
                LocalDate.now()
        ));
    }
}