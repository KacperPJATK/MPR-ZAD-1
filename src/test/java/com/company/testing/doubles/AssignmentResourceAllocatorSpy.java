package com.company.testing.doubles;

import model.Employee;
import service.ResourceAllocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@SuppressWarnings("all")
/**
 * Szpieg ResourceAllocator zbiera wszystkie przydziały zadań, aby testy mogły
 * zweryfikować jakie identyfikatory i pracownicy zostały przekazane.
 */
public class AssignmentResourceAllocatorSpy implements ResourceAllocator {
    private final List<TaskAssignment> assignments = new ArrayList<>();

    @Override
    public void assignTask(String id, List<Employee> employees) {
        assignments.add(new TaskAssignment(id, new ArrayList<>(employees)));
    }

    public List<TaskAssignment> getAssignments() {
        return new ArrayList<>(assignments);
    }

    public static final class TaskAssignment {
        private final String taskId;
        private final List<Employee> employees;

        public TaskAssignment(String taskId, List<Employee> employees) {
            this.taskId = taskId;
            this.employees = employees;
        }

        public String taskId() {
            return taskId;
        }

        public List<Employee> employees() {
            return employees;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (TaskAssignment) obj;
            return Objects.equals(this.taskId, that.taskId) &&
                    Objects.equals(this.employees, that.employees);
        }

        @Override
        public int hashCode() {
            return Objects.hash(taskId, employees);
        }

        @Override
        public String toString() {
            return "TaskAssignment[" +
                    "taskId=" + taskId + ", " +
                    "employees=" + employees + ']';
        }

    }
}
