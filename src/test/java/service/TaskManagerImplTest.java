package service;

import com.company.testing.doubles.AssignmentResourceAllocatorSpy;
import com.company.testing.doubles.AvailabilityProviderStub;
import com.company.testing.doubles.CompetencyProviderFake;
import com.company.testing.doubles.Dummy;
import model.Employee;
import model.Position;
import model.Skills;
import model.TaskDuration;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class TaskManagerImplTest {

    // Sprawdza, czy zadanie trafia do dostepnych i kompetentnych pracownikow w oczekiwanej kolejnosci.
    @Test
    void shouldAssignFirstAvailableCompetentEmployee() {
        Employee withoutSkill = employee("no-skill@example.com", Position.STAZYSTA);
        Employee competentFirst = employee("dev1@example.com", Position.PROGRAMISTA);
        Employee competentSecond = employee("dev2@example.com", Position.PROGRAMISTA);

        AvailabilityProviderStub availabilityStub = new AvailabilityProviderStub(
                List.of(withoutSkill, competentFirst, competentSecond)
        );
        CompetencyProviderFake competencyFake = new CompetencyProviderFake();
        competencyFake.addCompetency(Skills.JAVA, competentFirst, competentSecond);
        AssignmentResourceAllocatorSpy assignmentSpy = new AssignmentResourceAllocatorSpy();
        TaskManagerImpl taskManager = new TaskManagerImpl(availabilityStub, competencyFake, assignmentSpy);

        taskManager.assignTask("TASK-1", Skills.JAVA, sampleDuration());

        assertAll(
                () -> assertThat(assignmentSpy.getAssignments()).hasSize(1),
                () -> {
                    AssignmentResourceAllocatorSpy.TaskAssignment assignment = assignmentSpy.getAssignments().get(0);
                    assertAll(
                            () -> assertThat(assignment.taskId()).isEqualTo("TASK-1"),
                            () -> assertThat(assignment.employees()).containsExactly(competentFirst, competentSecond)
                    );
                }
        );

    }

    // Sprawdza, czy brak kompetentnych osob powoduje wyjatek i brak alokacji zadania.
    @Test
    void shouldFailWhenNoEmployeeMatchesRequestedSkills() {
        Employee available = employee("qa@example.com", Position.PROGRAMISTA);
        Dummy dummy = new Dummy("unused");
        AvailabilityProviderStub availabilityStub = new AvailabilityProviderStub(List.of(available));
        CompetencyProviderFake competencyFake = new CompetencyProviderFake();
        AssignmentResourceAllocatorSpy assignmentSpy = new AssignmentResourceAllocatorSpy();
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

}
