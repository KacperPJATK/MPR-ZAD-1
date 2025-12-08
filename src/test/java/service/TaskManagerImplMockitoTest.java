package service;

import model.Employee;
import model.Position;
import model.Skills;
import model.TaskDuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TaskManagerImplMockitoTest {

    @InjectMocks
    private TaskManagerImpl taskManager;

    @Mock
    private AvailabilityProvider availabilityProvider;

    @Mock
    private CompetencyProvider competencyProvider;

    @Mock
    private ResourceAllocator resourceAllocator;


    @Test
    void checksIfTaskIsAssigned() {
//        given
        TaskDuration taskDuration = taskDuration();

        List<Employee> available = List.of(
                employee("someTest@gmail.com", Position.PROGRAMISTA),
                employee("someOtherTest@gmail.com", Position.STAZYSTA)
        );

        List<Employee> competent = List.of(
                employee("someTest@gmail.com", Position.PROGRAMISTA)
        );

//        when
        Mockito.when(availabilityProvider.findAvailable(taskDuration)).thenReturn(available);
        Mockito.when(competencyProvider.findCompetent(available, Skills.JAVA)).thenReturn(competent);
        taskManager.assignTask("1", Skills.JAVA, taskDuration);
//        then
        Mockito.verify(resourceAllocator, Mockito.times(1)).assignTask(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(competent)
        );

    }

    @Test
    void shouldFailWhenThereAreNoAvailableEmployees() {
//        given
        TaskDuration taskDuration = taskDuration();
        Skills skill = Skills.JAVA;
//        when
        Mockito.when(availabilityProvider.findAvailable(taskDuration)).thenReturn(Collections.emptyList());
        Mockito.when(competencyProvider.findCompetent(Collections.emptyList(), skill))
                .thenReturn(Collections.emptyList());
//        then
        Assertions.assertThrows(
                RuntimeException.class,
                () -> taskManager.assignTask("1", skill, taskDuration)
        );

    }

    @Test
    void shouldFailWhenThereAreNoCompetentEmployees() {
//        given
        TaskDuration taskDuration = taskDuration();
        Skills skill = Skills.JAVA;
//        when
        Mockito.when(availabilityProvider.findAvailable(taskDuration)).thenReturn(Collections.emptyList());
        Mockito.when(competencyProvider.findCompetent(
                ArgumentMatchers.anyList(), ArgumentMatchers.eq(skill))
        ).thenReturn(Collections.emptyList());
//        then
        Assertions.assertThrows(
                RuntimeException.class,
                () -> taskManager.assignTask("1", skill, taskDuration)
        );

    }

    @Test
    void shouldFailWhenNull() {
//        given
        TaskDuration taskDuration = taskDuration();
        Skills skill = Skills.JAVA;
//        when
        Mockito.when(availabilityProvider.findAvailable(taskDuration)).thenReturn(null);
        Mockito.when(competencyProvider.findCompetent(
                ArgumentMatchers.anyList(),
                ArgumentMatchers.eq(skill)
        )).thenReturn(null);
//        then
        Assertions.assertThrows(
                RuntimeException.class,
                () -> taskManager.assignTask("1", skill, taskDuration)
        );

    }

    private Employee employee(String email, Position position) {
        return new Employee("Alex", "Doe", email, "Example", position, LocalDate.now());
    }

    private TaskDuration taskDuration() {
        return new TaskDuration(
                LocalDate.of(2024, 12, 14),
                LocalDate.of(2025, 12, 14)
        );
    }
}