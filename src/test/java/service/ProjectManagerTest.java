package service;

import exception.TeamException;
import model.Employee;
import model.Position;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import repository.EmployeesRepository;
import repository.ProjectRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ProjectManagerTest {

    private ProjectManager projectManager;

    private static void addTestEmployees() {
        EmployeesRepository.add(new Employee(
                "Leon", "Kennedy",
                "kennedy@protonmail.com", "Capcom", Position.STAZYSTA,
                LocalDate.now()
        ));
        EmployeesRepository.add(new Employee(
                "Leon", "Kennedy",
                "kennedy1@protonmail.com", "Capcom", Position.STAZYSTA,
                LocalDate.now()
        ));
        EmployeesRepository.add(new Employee(
                "Leon", "Kennedy",
                "kennedy2@protonmail.com", "Capcom", Position.STAZYSTA,
                LocalDate.now()
        ));
        EmployeesRepository.add(new Employee(
                "Chris", "Redfield",
                "redfield@protonmail.com", "Capcom", Position.PROGRAMISTA,
                LocalDate.now()
        ));
        EmployeesRepository.add(new Employee(
                "Chris", "Redfield",
                "redfield1@protonmail.com", "Capcom", Position.PROGRAMISTA,
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

    public static Stream<Arguments> getTestDataForCreateTeam() {
        addTestEmployees();
        return Stream.of(
                Arguments.of("Pros", EmployeesRepository.getEmployees(), true),
                Arguments.of("Noobs", new ArrayList<Employee>(), false)
        );
    }

    public static Stream<Arguments> createProjectFailData() {
        addTestEmployees();
        return Stream.of(
                Arguments.of("NotEnoughStazysta", prepareFailList(0)),
                Arguments.of("NotEnoughProgamista", prepareFailList(2)),
                Arguments.of("NotEnoughManager", prepareFailList(5)),
                Arguments.of("ToManyWiceprezes", prepareFailList(6)),
                Arguments.of("ToManyPrezes", prepareFailList(7)),
                Arguments.of("ToManyPeople", prepareFailList(8))
        );
    }

    private static List<Employee> prepareFailList(int i) {
        List<Employee> employees = new ArrayList<>(EmployeesRepository.getEmployees());
        switch (i) {
            case 0:
                employees.removeFirst();
                employees.remove(1);
                return employees;
            case 2:
            case 5:
                employees.remove(EmployeesRepository.getEmployee("ndrake@protonmail.com"));
                return employees;
            case 6:
                Employee employee = new Employee(
                        "Sam", "Drake",
                        "sdrake1@protonmail.com", "NaughtyDog", Position.WICEPREZES,
                        LocalDate.now()
                );
                employees.add(employee);
                return employees;
            case 7:
                Employee employee1 = new Employee(
                        "Sam", "Drake",
                        "sdrake1@protonmail.com", "NaughtyDog", Position.PREZES,
                        LocalDate.now()
                );
                employees.add(employee1);
                return employees;
            case 8:
                Employee employeeA = new Employee(
                        "a", "a", "a@mail.com", "acom",
                        Position.PROGRAMISTA, LocalDate.now());

                Employee employeeB = new Employee(
                        "b", "b", "b@mail.com", "bcom",
                        Position.PROGRAMISTA, LocalDate.now());
                Employee employeeC = new Employee(
                        "c", "c", "c@mail.com", "ccom",
                        Position.PROGRAMISTA, LocalDate.now());
                employees.add(employeeA);
                employees.add(employeeB);
                employees.add(employeeC);
                return employees;
            default:
                return Collections.emptyList();
        }
    }


    @BeforeEach
    void setup() {
        projectManager = new ProjectManagerImpl();
    }

    @AfterEach
    void clear() {
        EmployeesRepository.clearForTest();
        ProjectRepository.clearForTest();
    }

    @ParameterizedTest
    @MethodSource("getTestDataForCreateTeam")
    void createTeamTest(String teamName, List<Employee> assignedToProject, boolean expected) {
//        when, then
        Assertions.assertThat(projectManager.createTeam(teamName, assignedToProject))
                .isEqualTo(expected);
    }

    @Test
    void shouldFailWhenTryingToCreateProjectWithAlreadyExistingName() {
//        given
        addTestEmployees();
        List<Employee> employees = EmployeesRepository.getEmployees();
        projectManager.createTeam("Existing", employees);
//        when, then
        Assertions.assertThatThrownBy(() -> projectManager.createTeam("Existing", employees))
                .isInstanceOf(TeamException.class);
    }

    @ParameterizedTest
    @MethodSource("createProjectFailData")
    void shouldFailWhenTeamDoesNotMeetTheMinimalRequirements(String teamName, List<Employee> failList) {
//        Min 2 Stażystów
//        Min 2 Programistów
//        Min 1 Manager
//        Max 1 Wiceprezes
//        Max 1 Prezes
//        Max 9 Pracowników
//        when, then
        Assertions.assertThatThrownBy(() -> projectManager.createTeam(teamName, failList))
                .isInstanceOf(TeamException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "miller@protonmail.com, Team A, Team B",
            "kennedy1@protonmail.com, Team B, Team A"
    })
    void moveEmployeeToAnotherTeamTest(String email, String from, String to) {
//        given
        prepareTeams();
//        when, then
        Assertions.assertThat(projectManager.moveEmployee(email, from, to))
                .isEqualTo(true);
    }


    @ParameterizedTest
    @CsvSource({
            "millerdoesntexist@protonmail.com, Team A, Team B",
            "miller@protonmail.com, Team B, Team A"
    })
    void shouldFailWhenTryingToMoveNonExistingEmployeeBetweenTeams(String email, String from, String to) {
//        given
        prepareTeams();
//        when, then
        Assertions.assertThatThrownBy(() -> projectManager.moveEmployee(email, from, to))
                .isInstanceOf(TeamException.class);
    }


    @ParameterizedTest
    @CsvSource({
            "miller@protonmail.com, Team D, Team B",
            "kennedy1@protonmail.com, Team B, Team D"
    })
    void shouldFailWhenTryingToMoveEmployeeBetweenNonExistingTeams(String email, String from, String to) {
//        given
        prepareTeams();
//        when, then
        Assertions.assertThatThrownBy(() -> projectManager.moveEmployee(email, from, to))
                .isInstanceOf(TeamException.class);
    }


    @ParameterizedTest
    @CsvSource({
            "ndrake@protonmail.com, Team A, Team B",
            "sdrake@protonmail.com, Team B, Team A",
            "redfield@protonmail.com, Team A, Team B"
    })
    void shouldFailWhenTryingToMoveEmployeeBreaksTheMinimalRequirements(String email, String from, String to) {
//        given
        prepareTeams();
//        when, then
        Assertions.assertThatThrownBy(() -> projectManager.moveEmployee(email, from, to))
                .isInstanceOf(TeamException.class);
    }


    @ParameterizedTest
    @CsvSource({
            "sdrake@protonmail.com, Team B, Team A"
    })
    void shouldFailWhenTryingToMoveEmployeeExceedsMaxTeamSize(String email, String from, String to) {
//        given
        prepareTeams();
        projectManager.moveEmployee("kennedy@protonmail.com", "Team B", "Team A");
//        when, then
        Assertions.assertThatThrownBy(() -> projectManager.moveEmployee(email, from, to))
                .isInstanceOf(TeamException.class);
    }


    private void prepareTeams() {
        addTestEmployees();

        List<Employee> all = new ArrayList<>(EmployeesRepository.getEmployees());
        List<Employee> teamA = new ArrayList<>(all);
        List<Employee> teamB = new ArrayList<>(all);
        teamB.removeLast();

        projectManager.createTeam("Team A", teamA);
        projectManager.createTeam("Team B", teamB);
    }


}
