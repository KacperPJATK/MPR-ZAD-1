package service;

import model.Employee;
import model.Skills;

import java.util.List;

public interface CompetencyProvider {
    List<Employee> findCompetent(List<Employee> available, Skills skills);
}
