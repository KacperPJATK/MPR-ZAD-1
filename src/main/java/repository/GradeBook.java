package repository;

import model.Grade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradeBook {
    private static final Map<String, List<Grade>> gradeBook = new HashMap<>();


    public static void assign(String email, Grade grade) {
        if (gradeBook.containsKey(email)) {
            List<Grade> grades = gradeBook.get(email);
            grades.add(grade);
        } else {
            List<Grade> grades = new ArrayList<>();
            grades.add(grade);
            gradeBook.put(email, grades);
        }
    }

    public static List<Grade> getGrades(String email) {
        return gradeBook.get(email);
    }

    public static void clearForTest() {
        gradeBook.clear();
    }

    public static Map<String, List<Grade>> getGradeBook() {
        return Map.copyOf(gradeBook);
    }
}
