package com.company.testing.doubles;

import model.Employee;
import model.TaskDuration;
import service.AvailabilityProvider;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
/**
 * Stub AvailabilityProvider zwracajacy zawsze z gory ustalona liste dostepnych pracownikow,
 * dzieki czemu testy TaskManagera omijaja realny kalendarz.
 */
public class AvailabilityProviderStub implements AvailabilityProvider {
    private final List<Employee> available;

    public AvailabilityProviderStub(List<Employee> available) {
        this.available = new ArrayList<>(available);
    }

    @Override
    public List<Employee> findAvailable(TaskDuration duration) {
        return new ArrayList<>(available);
    }
}
