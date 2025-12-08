package service;

import model.Employee;

import java.util.List;
import java.util.Map;

public interface ResourceAllocator {
    void assignTask(String id, List<Employee> employees);
}
