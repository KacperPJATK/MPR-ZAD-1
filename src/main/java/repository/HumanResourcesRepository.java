package repository;

import model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HumanResourcesRepository {

    public static final Map<String, List<Employee>> ASSIGNED = new HashMap<>();

    public static void assignTask(String id, List<Employee> competent) {
        ASSIGNED.put(id, competent);
    }
}
