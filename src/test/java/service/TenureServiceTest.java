package service;

import model.Employee;
import model.Position;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import repository.EmployeesRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class TenureServiceTest {

    private TenureService tenureService;

    private static void addTestEmployees() {
        EmployeesRepository.add(new Employee(
                "Leon", "Kennedy",
                "kennedy@protonmail.com", "Capcom", Position.STAZYSTA,
                LocalDate.parse("2024-10-12")
        ));
        EmployeesRepository.add(new Employee(
                "Chris", "Redfield",
                "redfield@protonmail.com", "Capcom", Position.PROGRAMISTA,
                LocalDate.parse("2015-09-20")
        ));
        EmployeesRepository.add(new Employee(
                "Nathan", "Drake",
                "ndrake@protonmail.com", "NaughtyDog", Position.MANAGER,
                LocalDate.parse("2020-05-01")
        ));
        EmployeesRepository.add(new Employee(
                "Sam", "Drake",
                "sdrake@protonmail.com", "NaughtyDog", Position.WICEPREZES,
                LocalDate.parse("2025-11-22")
        ));
    }

    public static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(1, 3),
                Arguments.of(2, 2),
                Arguments.of(5, 2),
                Arguments.of(10, 1),
                Arguments.of(15, 0)
        );
    }

    @BeforeEach
    void setup() {
        EmployeesRepository.clearForTest();
        tenureService = new TenureServiceImpl();
        addTestEmployees();
    }

    @AfterEach
    void clear() {
        EmployeesRepository.clearForTest();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "kennedy@protonmail.com",
            "redfield@protonmail.com",
            "ndrake@protonmail.com",
            "kennedy@protonmail.com"
    })
    void testCalculateTenure(String email) {
//        given
        Employee employee = EmployeesRepository.getEmployee(email);
        long expected = Period.between(employee.getEmploymentDate(), LocalDate.now()).getYears();
//        when, then
        MatcherAssert.assertThat(
                tenureService.calculateTenure(email),
                Matchers.is(Matchers.equalTo(expected))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "kennedydoesntexist@protonmail.com",
            "redfielddoesntexist@protonmail.com",
            "ndrakedoesntexist@protonmail.com",
            "kennedydoesntexist@protonmail.com"
    })
    void shouldFailWhenTryingToCalculateTenureForNonExistingEmployee(String email) {
//        when, then
        Assertions.assertThatThrownBy(() -> tenureService.calculateTenure(email))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldFailWhenTryingToCalculateTenureForNull() {
//        when, then
        Assertions.assertThatThrownBy(() -> tenureService.calculateTenure(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testGetAnniversaries() {
//        when, then
        MatcherAssert.assertThat(tenureService.getAnniversaries(), Matchers.hasItems(
                        Matchers.hasProperty(
                                "email",
                                Matchers.is(Matchers.equalTo("ndrake@protonmail.com"))
                        ),
                        Matchers.hasProperty(
                                "email",
                                Matchers.is(Matchers.equalTo("redfield@protonmail.com"))
                        )
                )
        );
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoEmployees() {
//        given
        EmployeesRepository.clearForTest();
//        when, then
        MatcherAssert.assertThat(tenureService.getAnniversaries(), Matchers.hasSize(0));
    }

    @ParameterizedTest
    @MethodSource("testData")
    void getEmployeesWithTenureLength(int tenureLength, int expectedSize) {
//        when, then
        MatcherAssert.assertThat(
                tenureService.getEmployeesWithTenureLength(tenureLength),
                Matchers.hasSize(expectedSize)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "Capcom, 2",
            "NaughtyDog, 2"
    })
    void testGetDemographicTeamReport(String companyName, int size) {
//        when, then
        MatcherAssert.assertThat(
                tenureService.getDemographicReport(companyName),
                Matchers.allOf(
                        Matchers.hasProperty(
                                "companyName", Matchers.is(Matchers.equalTo(companyName))
                        ),
                        Matchers.hasProperty("tenureMap", Matchers.aMapWithSize(size))
                )
        );
    }

    @Test
    void shouldFailWhenTryingToCreateDemographicReportWhenThereAreNoEmployees() {
//        given
        EmployeesRepository.clearForTest();
//        when, then
        Assertions.assertThatThrownBy(() -> tenureService.getDemographicReport("company"))
                .isInstanceOf(NoSuchElementException.class);


    }

}
