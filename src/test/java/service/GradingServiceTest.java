package service;

import model.Employee;
import model.Grade;
import model.Position;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import repository.EmployeesRepository;
import repository.GradeBook;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class GradingServiceTest {

    private GradingService gradingService;

    private static void addTestEmployees() {
        EmployeesRepository.add(new Employee(
                "Leon", "Kennedy",
                "kennedy@protonmail.com", "Capcom", Position.STAZYSTA,
                LocalDate.now()
        ));
        EmployeesRepository.add(new Employee(
                "Chris", "Redfield",
                "redfield@protonmail.com", "Capcom", Position.PROGRAMISTA,
                LocalDate.now()
        ));
        EmployeesRepository.add(new Employee(
                "Nathan", "Drake",
                "ndrake@protonmail.com", "NaughtyDog", Position.MANAGER,
                LocalDate.now()
        ));
        EmployeesRepository.add(new Employee(
                "Sam", "Drake",
                "sdrake@protonmail.com", "NaughtyDog", Position.WICEPREZES,
                LocalDate.now()
        ));
        EmployeesRepository.add(new Employee(
                "Joel", "Miller",
                "miller@protonmail.com", "NaughtyDog", Position.PREZES,
                LocalDate.now()
        ));
    }

    public static Stream<Arguments> getAverageGradeTestData() {
        return Stream.of(
                Arguments.of("ndrake@protonmail.com", BigDecimal.valueOf(4)),
                Arguments.of("sdrake@protonmail.com", BigDecimal.valueOf(3))
        );
    }

    @AfterEach
    void cleanRepository() {
        EmployeesRepository.clearForTest();
        GradeBook.clearForTest();
    }

    @BeforeEach
    void setUP() {
        gradingService = new GradingServiceImpl();
        addTestEmployees();
    }

    @ParameterizedTest
    @CsvSource({
            "kennedy@protonmail.com, FIVE",
            "redfield@protonmail.com, FOUR",
            "ndrake@protonmail.com, THREE",
            "sdrake@protonmail.com, TWO",
            "miller@protonmail.com, ONE"
    })
    void testAssignGradeWhenEmployeeHasNoGrades(String email, Grade grade) {
//        when, then
        Assertions.assertThat(gradingService.assignGrade(email, grade)).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "kennedydontexist@protonmail.com, FIVE",
            "redfielddontexist@protonmail.com, FOUR",
            "null, THREE",
            "sdrakedontexist@protonmail.com, TWO",
            "null, ONE"}, nullValues = "null"
    )
    void shouldFailWhenTryingToAssignGradeToNonExistingEmployee(String email, Grade grade) {
//        when, then
        Assertions.assertThatThrownBy(() -> gradingService.assignGrade(email, grade))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "ndrake@protonmail.com, THREE, 2",
            "sdrake@protonmail.com, FIVE, 2",
    })
    void testAssignGradeWhenEmployeeHasGrades(String email, Grade grade, int size) {
//        given
        gradingService.assignGrade("ndrake@protonmail.com", Grade.FIVE);
        gradingService.assignGrade("sdrake@protonmail.com", Grade.THREE);
//        when, then
        SoftAssertions assertions = new SoftAssertions();

        assertions.assertThat(gradingService.assignGrade(email, grade)).isTrue();

        assertions.assertThat(gradingService.getGrades(email)).isNotEmpty();

        assertions.assertThat(gradingService.getGrades(email)).hasSize(size);

        assertions.assertAll();
    }

    @ParameterizedTest
    @MethodSource("getAverageGradeTestData")
    void testGetAverageGrade(String email, BigDecimal avg) {
//        given
        prepareGradeBook();
//        when, then
        Assertions.assertThat(gradingService.getAverageGrade(email)).isEqualTo(avg);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ndrakedoesntexist@protonmail.com", "21drake@protonmail.com"})
    void shouldFailWhenTryingToGetAverageGradeFromNonExistingUser(String email) {
//        given
        prepareGradeBook();
//        when, then
        Assertions.assertThatThrownBy(() -> gradingService.getAverageGrade(email))
                .isInstanceOf(NoSuchElementException.class);
    }

    private void prepareGradeBook() {
        gradingService.assignGrade("ndrake@protonmail.com", Grade.FIVE);
        gradingService.assignGrade("sdrake@protonmail.com", Grade.THREE);
        gradingService.assignGrade("ndrake@protonmail.com", Grade.THREE);
        gradingService.assignGrade("sdrake@protonmail.com", Grade.THREE);
    }

    @Test
    void testGetBestEmployees() {
//        given
        prepareGradeBook();
//        when, then
        Assertions.assertThat(gradingService.getBestEmployees())
                .extracting(Employee::getEmail)
                .containsExactlyInAnyOrder("ndrake@protonmail.com", "sdrake@protonmail.com");
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoGradedEmployees() {
//        when, then
        Assertions.assertThat(gradingService.getBestEmployees()).isEmpty();
    }
}
