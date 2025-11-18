package service;

import model.Employee;
import model.Grade;
import model.Pair;
import repository.EmployeesRepository;
import repository.GradeBook;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class GradingServiceImpl implements GradingService {
    @Override
    public boolean assignGrade(String email, Grade grade) {

        if (Objects.isNull(email) || Objects.isNull(grade)) {
            String message = String.format("email: %s, grade: %s", email, grade);
            throw new IllegalArgumentException(message);
        }

        if (!EmployeesRepository.containsEmail(email)) {
            throw new IllegalArgumentException("No such user");
        }

        GradeBook.assign(email, grade);
        return true;
    }

    @Override
    public List<Grade> getGrades(String email) {
        return GradeBook.getGrades(email);
    }

    @Override
    public BigDecimal getAverageGrade(String email) {
        if (!EmployeesRepository.containsEmail(email)) {
            String message = String.format("No such email: %s", email);
            throw new NoSuchElementException(message);
        }

        List<Grade> grades = GradeBook.getGrades(email);
        Optional<BigDecimal> reduced = grades.stream()
                .map(Grade::getValue)
                .reduce(BigDecimal::add);
        if (reduced.isPresent()) {
            return reduced.get().divide(BigDecimal.valueOf(grades.size()), RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<Employee> getBestEmployees() {
        Map<String, List<Grade>> gradeBook = GradeBook.getGradeBook();
        return gradeBook.entrySet().stream()
                .map(this::getPair)
                .sorted(Comparator.comparing(Pair::getValue))
                .map(pair -> EmployeesRepository.getEmployee(pair.getKey()))
                .toList();

    }

    private Pair<String, BigDecimal> getPair(Map.Entry<String, List<Grade>> entry) {
        return new Pair<>(entry.getKey(), getAverageGrade(entry.getKey()));
    }
}
