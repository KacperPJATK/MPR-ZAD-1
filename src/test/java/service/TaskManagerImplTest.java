package service;

import model.Employee;
import model.Position;
import model.Skills;
import model.TaskDuration;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TaskManagerImplTest {

    @Test
    void shouldAssignFirstAvailableCompetentEmployee() {
        Employee withoutSkill = employee("no-skill@example.com", Position.STAZYSTA);
        Employee competentFirst = employee("dev1@example.com", Position.PROGRAMISTA);
        Employee competentSecond = employee("dev2@example.com", Position.PROGRAMISTA);
        DummyConfig dummyConfig = new DummyConfig("unused-region");

        AvailabilityCalendarStub availabilityStub = new AvailabilityCalendarStub(
                List.of(withoutSkill, competentFirst, competentSecond),
                dummyConfig
        );
        CompetencyRepositoryFake competencyFake = new CompetencyRepositoryFake();
        competencyFake.addCompetency(Skills.JAVA, competentFirst, competentSecond);
        AssignmentSpy assignmentSpy = new AssignmentSpy();
        TaskManagerImpl taskManager = new TaskManagerImpl(availabilityStub, competencyFake, assignmentSpy);

        taskManager.assignTask("TASK-1", Skills.JAVA, sampleDuration());

        assertAll(
                () -> assertThat(assignmentSpy.getAssignments()).hasSize(1),
                () -> {
                    AssignmentSpy.TaskAssignment assignment = assignmentSpy.getAssignments().get(0);
                    assertAll(
                            () -> assertThat(assignment.taskId()).isEqualTo("TASK-1"),
                            () -> assertThat(assignment.employees()).containsExactly(competentFirst, competentSecond)
                    );
                }
        );

    }

    @Test
    void shouldFailWhenNoEmployeeMatchesRequestedSkills() {
        Employee available = employee("qa@example.com", Position.PROGRAMISTA);
        DummyConfig dummyConfig = new DummyConfig("unused");
        AvailabilityCalendarStub availabilityStub = new AvailabilityCalendarStub(List.of(available), dummyConfig);
        CompetencyRepositoryFake competencyFake = new CompetencyRepositoryFake();
        AssignmentSpy assignmentSpy = new AssignmentSpy();
        TaskManagerImpl taskManager = new TaskManagerImpl(availabilityStub, competencyFake, assignmentSpy);

        assertAll(
                () -> assertThatThrownBy(() -> taskManager.assignTask("TASK-404", Skills.DEVOPS, sampleDuration()))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("no employees"),
                () -> assertThat(assignmentSpy.getAssignments()).isEmpty()
        );
    }

    private TaskDuration sampleDuration() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        return new TaskDuration(start, start.plusDays(5));
    }

    private Employee employee(String email, Position position) {
        return new Employee("Alex", "Doe", email, "Example", position, LocalDate.now());
    }

    private record DummyConfig(String region) {
    }

    private static final class AvailabilityCalendarStub implements AvailabilityProvider {
        private final List<Employee> available;
        private final DummyConfig config;

        AvailabilityCalendarStub(List<Employee> available, DummyConfig config) {
            this.available = available;
            this.config = config;
        }

        @Override
        public List<Employee> findAvailable(TaskDuration duration) {
            return available;
        }
    }

    private static final class CompetencyRepositoryFake implements CompetencyProvider {
        private final Map<Skills, List<Employee>> skills = new LinkedHashMap<>();

        void addCompetency(Skills skill, Employee... employees) {
            skills.put(skill, List.of(employees));
        }

        @Override
        public List<Employee> findCompetent(List<Employee> available, Skills skillsToFind) {
            List<Employee> withSkill = skills.getOrDefault(skillsToFind, List.of());
            return available.stream()
                    .filter(withSkill::contains)
                    .toList();
        }
    }

    private static final class AssignmentSpy implements ResourceAllocator {
        private final List<TaskAssignment> assignments = new ArrayList<>();

        @Override
        public void assignTask(String id, List<Employee> employees) {
            assignments.add(new TaskAssignment(id, new ArrayList<>(employees)));
        }

        List<TaskAssignment> getAssignments() {
            return new ArrayList<>(assignments);
        }

        private record TaskAssignment(String taskId, List<Employee> employees) {
        }
    }
}
