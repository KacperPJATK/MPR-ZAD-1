package repository;

import model.Employee;
import service.ResourceAllocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HumanResourcesRepository implements ResourceAllocator {

    public static final Map<String, List<Employee>> ASSIGNED = new HashMap<>();

    @Override
    public void assignTask(String id, List<Employee> competent) {
        ASSIGNED.put(id, competent);
    }
}
