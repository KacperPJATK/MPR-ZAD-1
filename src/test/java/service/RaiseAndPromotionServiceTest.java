package service;

import model.Employee;
import model.Position;
import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import repository.EmployeesRepository;

import java.math.BigDecimal;
import java.util.stream.Stream;

class RaiseAndPromotionServiceTest {

    private RaiseAndPromotionService service;

    private static void addTestEmployees() {
        EmployeesRepository.add(new Employee(
                "Leon", "Kennedy",
                "kennedy@protonmail.com", "Capcom", Position.STAZYSTA
        ));
        EmployeesRepository.add(new Employee(
                "Chris", "Redfield",
                "redfield@protonmail.com", "Capcom", Position.PROGRAMISTA
        ));
        EmployeesRepository.add(new Employee(
                "Nathan", "Drake",
                "ndrake@protonmail.com", "NaughtyDog", Position.MANAGER
        ));
        EmployeesRepository.add(new Employee(
                "Sam", "Drake",
                "sdrake@protonmail.com", "NaughtyDog", Position.WICEPREZES
        ));
        EmployeesRepository.add(new Employee(
                "Joel", "Miller",
                "miller@protonmail.com", "NaughtyDog", Position.PREZES
        ));
    }

    public static Stream<Arguments> positionTestData() {
        return Stream.of(
                Arguments.of("kennedy@protonmail.com", Position.PROGRAMISTA),
                Arguments.of("redfield@protonmail.com", Position.MANAGER),
                Arguments.of("ndrake@protonmail.com", Position.WICEPREZES),
                Arguments.of("sdrake@protonmail.com", Position.PREZES)
        );
    }

    @AfterEach
    void cleanRepository() {
        EmployeesRepository.clearForTest();
    }

    @BeforeEach
    void setUP() {
        service = new RaiseAndPromotionServiceImpl();
        addTestEmployees();
    }

    @ParameterizedTest
    @MethodSource("positionTestData")
    @DisplayName("Checks if position was changed to a new one")
    void testPositionAfterPromotion(String email, Position position) {
//        when
        service.promote(email, position);
//        then
        MatcherAssert.assertThat(
                EmployeesRepository.getEmployeeForTest(email).getPosition(),
                Matchers.is(Matchers.equalTo(position))
        );
    }

    @ParameterizedTest
    @MethodSource("positionTestData")
    @DisplayName("Checks if hierarchyLevel is correct")
    void testHierarchyLevelAfterPromotion(String email, Position position) {
//        when
        service.promote(email, position);
//        then
        MatcherAssert.assertThat(
                EmployeesRepository.getEmployeeForTest(email).getPosition().getHierarchyLevel(),
                Matchers.is(Matchers.equalTo(position.getHierarchyLevel()))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "kennedy@protonmail.com, Position.PROGRAMISTA",
            "redfield@protonmail.com, Position.MANAGER",
            "ndrake@protonmail.com, Position.WICEPREZES",
            "sdrake@protonmail.com, Position.PREZES"
    })
    @DisplayName("Checks if salary is assigned correctly")
    void testAssignedSalaryAfterPromotion(String email, Position position) {
//        when
        service.promote(email, position);
//        then
        MatcherAssert.assertThat(
                EmployeesRepository.getEmployeeForTest(email).getPosition().getSalary(),
                Matchers.is(Matchers.equalTo(position.getSalary()))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "kennedy@protonmail.com, Position.Manager",
            "redfield@protonmail.com, Position.PREZES"
    })
    void shouldFailWhenPromotingByMoreThanPosition(String email, Position position) {
//        when,then
        Assertions.assertThatThrownBy(() -> service.promote(email, position))
                .isInstanceOf(PromotionException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "null, Position.Manager",
            "redfield@protonmail.com, null"
    })
    void shouldFailWhenPromotingNull(String email, Position position) {
//        when,then
        Assertions.assertThatThrownBy(() -> service.promote(email, position))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldFailWhenPromotingNonExistingUser() {
//        given
        String email = "idontexist@protonmail.com";
        Position position = Position.PREZES;
//        when,then
        Assertions.assertThatThrownBy(() -> service.promote(email, position))
                .isInstanceOf(PromotionException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "ndrake@protonmail.com, Position.STAZYSTA",
            "sdrake@protonmail.com, Position.WICEPREZES"
    })
    void shouldFailWhenTryingToDemoteOrPromoteToTheSameLevel(String email, Position position) {
//        when,then
        Assertions.assertThatThrownBy(() -> service.promote(email, position))
                .isInstanceOf(PromotionException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "kennedy@protonmail.com, 100, 6000",
            "redfield@protonmail.com, 40, 11200",
            "ndrake@protonmail.com, 30, 15600",
            "sdrake@protonmail.com, 20, 21600",
            "miller@protonmail.com, 10, 27500"
    })
    void testRaise(String email, int percent, BigDecimal result) {
//        given
        Employee employee = EmployeesRepository.getEmployeeForTest(email);
//        when
        service.giveRaise(email, percent);
//        then
        Assertions.assertThat(employee.getSalary()).isEqualTo(result);
    }

    @ParameterizedTest
    @DisplayName("Should fail when salary after promotion exceeds one of the the higher ranked employee")
    @CsvSource({
            "kennedy@protonmail.com, 110",
            "redfield@protonmail.com, 60",
            "ndrake@protonmail.com, 70",
            "sdrake@protonmail.com, 80",
            "miller@protonmail.com, 90"
    })
    void shouldFailWhenExceeds(String email, int percent) {
//        when, then
        Assertions.assertThatThrownBy(() -> service.giveRaise(email, percent))
                .isInstanceOf(RaiseException.class);
    }

    @ParameterizedTest
    @DisplayName("Should fail when giving a 0% raise")
    @CsvSource({
            "kennedy@protonmail.com, 0",
            "redfield@protonmail.com, 0",
            "ndrake@protonmail.com, 0",
            "sdrake@protonmail.com, 0",
            "miller@protonmail.com, 0"
    })
    void shouldFailWhenGivingAZeroPercentRaise(String email, int percent) {
//        when, then
        Assertions.assertThatThrownBy(() -> service.giveRaise(email, percent))
                .isInstanceOf(RaiseException.class);
    }

    @ParameterizedTest
    @DisplayName("Should fail when giving a raise to a non existing employee")
    @CsvSource({
            "kennedydoesntexist@protonmail.com, 100",
            "redfielddoesntexist@protonmail.com, 40",
            "ndrakedoesntexist@protonmail.com, 30",
            "sdrakedoesntexist@protonmail.com, 20",
            "millerdoesntexist@protonmail.com, 10"
    })
    void shouldFailWhenGivingARaiseToANonExistingEmployee(String email, int percent) {
//        when, then
        Assertions.assertThatThrownBy(() -> service.giveRaise(email, percent))
                .isInstanceOf(RaiseException.class);
    }

    @ParameterizedTest
    @DisplayName("Should fail when giving a raise to a null")
    @CsvSource({
            "null, 100",
            "null, 40",
            "null, 30",
            "null, 20",
            "null, 10"
    })
    void shouldFailWhenGivingARaiseToNULL(String email, int percent) {
//        when, then
        Assertions.assertThatThrownBy(() -> service.giveRaise(email, percent))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
