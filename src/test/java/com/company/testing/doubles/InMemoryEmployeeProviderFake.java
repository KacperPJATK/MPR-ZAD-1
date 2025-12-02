package com.company.testing.doubles;

import model.Employee;
import service.EmployeeProvider;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
/**
 * Fake EmployeeProvider przechowujacy pracownikow w pamieci, aby testy mogly
 * odczytywac kontrolowana liste bez dostepu do repozytorium lub bazy.
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
