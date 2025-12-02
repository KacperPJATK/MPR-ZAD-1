package service;

import model.Employee;

import java.util.List;

public interface ResourceAllocator {
    void assignTask(String id, List<Employee> employees);
}
