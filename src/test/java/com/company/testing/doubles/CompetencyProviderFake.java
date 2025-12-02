package com.company.testing.doubles;

import model.Employee;
import model.Skills;
import service.CompetencyProvider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
/**
 * Fake CompetencyProvider przechowujacy kompetencje w pamieci, aby testy mogly
 * dowolnie ustawiac, ktorzy pracownicy spelniaja wymagane umiejetnosci.
 */
public class CompetencyProviderFake implements CompetencyProvider {
    private final Map<Skills, List<Employee>> skills = new LinkedHashMap<>();

    public void addCompetency(Skills skill, Employee... employees) {
        skills.put(skill, List.of(employees));
    }

    @Override
    public List<Employee> findCompetent(List<Employee> available, Skills skillsToFind) {
        List<Employee> withSkill = skills.getOrDefault(skillsToFind, List.of());
        return available.stream()
                .filter(withSkill::contains)
                .toList();
    }
}
