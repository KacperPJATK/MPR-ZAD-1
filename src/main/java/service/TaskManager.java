package service;

import model.Skills;
import model.TaskDuration;

public interface TaskManager {
    void assignTask(String id, Skills skills, TaskDuration taskDuration);
}
