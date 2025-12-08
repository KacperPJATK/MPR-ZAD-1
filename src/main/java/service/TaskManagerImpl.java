package service;

import model.Employee;
import model.Skills;
import model.TaskDuration;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerImpl implements TaskManager {
    private final AvailabilityProvider availabilityProvider;
    private final CompetencyProvider competencyProvider;
    private final ResourceAllocator resourceAllocator;


    TaskManagerImpl(AvailabilityProvider availabilityProvider,
                    CompetencyProvider competencyProvider,
                    ResourceAllocator resourceAllocator
    ) {
        this.availabilityProvider = availabilityProvider;
        this.competencyProvider = competencyProvider;
        this.resourceAllocator = resourceAllocator;
    }

    @Override
    public void assignTask(String id, Skills skills, TaskDuration taskDuration) {
        List<Employee> available = availabilityProvider.findAvailable(taskDuration);
        List<Employee> competent = competencyProvider.findCompetent(available, skills);
        if (competent == null || competent.isEmpty()) {
            throw new RuntimeException("There are no employees, that suit that request");
        }
        resourceAllocator.assignTask(id, new ArrayList<>(competent));
    }
}
