package service;

import model.Employee;
import model.Grade;

import java.math.BigDecimal;
import java.util.List;

public interface GradingService {
    boolean assignGrade(String email, Grade grade);

    List<Grade> getGrades(String email);

    BigDecimal getAverageGrade(String email);

    List<Employee> getBestEmployees();

}
