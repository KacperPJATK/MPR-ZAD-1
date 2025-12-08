package repository;

import model.Employee;
import model.Pair;
import model.TaskDuration;
import service.AvailabilityProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvailabilityCalendar implements AvailabilityProvider {

    private static final List<Pair<TaskDuration, Employee>> CALENDAR = getDefaultCalendar();

    private static List<Pair<TaskDuration, Employee>> getDefaultCalendar() {
        List<Employee> employees = EmployeesRepository.getEmployees();
        return employees.stream()
                .map(employee -> new Pair<>(new TaskDuration(LocalDate.EPOCH, LocalDate.EPOCH), employee))
                .toList();
    }

    @Override
    public List<Employee> findAvailable(TaskDuration duration) {

        List<Employee> available = new ArrayList<>();
        LocalDate newStart = duration.getStart();

        for (Pair<TaskDuration, Employee> pair : CALENDAR) {
            TaskDuration current = pair.getKey();
            Employee employee = pair.getValue();

            boolean neverAssigned = current.getStart().equals(LocalDate.EPOCH);
            boolean finishedBeforeNew = !neverAssigned && current.getEnd().isBefore(newStart);

            if (neverAssigned || finishedBeforeNew) {
                available.add(employee);

                current.setStart(duration.getStart());
                current.setEnd(duration.getEnd());
            }
        }

        return available.isEmpty() ? Collections.emptyList() : available;
    }

}

