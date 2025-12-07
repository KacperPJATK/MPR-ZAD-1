package com.company.testing.doubles;

import model.Employee;
import service.EmployeeProvider;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
/**
 * Fake EmployeeProvider przechowujący pracowników w pamięci, aby testy mogły
 * odczytywać liste bez dostępu do repozytorium lub bazy.
 */
public class InMemoryEmployeeProviderFake implements EmployeeProvider {
    private final List<Employee> employees;

    public InMemoryEmployeeProviderFake(List<Employee> employees) {
        this.employees = new ArrayList<>(employees);
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees);
    }
}
