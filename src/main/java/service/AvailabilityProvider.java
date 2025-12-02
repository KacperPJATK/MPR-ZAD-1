package service;

import model.Employee;
import model.TaskDuration;

import java.util.List;

public interface AvailabilityProvider {
    List<Employee> findAvailable(TaskDuration duration);
}
