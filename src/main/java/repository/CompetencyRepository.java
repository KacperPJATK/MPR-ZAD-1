package repository;

import model.Employee;
import model.Skills;
import service.CompetencyProvider;

import java.util.*;

public class CompetencyRepository implements CompetencyProvider {

    private static final Map<Skills, List<Employee>> SKILLS = new HashMap<>();

    @Override
    public List<Employee> findCompetent(List<Employee> available, Skills skills) {
        List<Employee> contenders = SKILLS.get(skills);
        if (contenders == null || contenders.isEmpty()) {
            return Collections.emptyList();
        }
        List<Employee> competentAndAvailable = new ArrayList<>();
        for (Employee contender : contenders) {
            for (Employee employee : available) {
                if (contender.equals(employee)) {
                    competentAndAvailable.add(contender);
                }
            }
        }
        return competentAndAvailable.isEmpty() ? Collections.emptyList() : competentAndAvailable;
    }
}
