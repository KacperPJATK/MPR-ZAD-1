package model;

import java.time.LocalDate;
import java.util.Objects;

public class Certification {
    private final Employee employee;
    private final String type;
    private final LocalDate expiryDate;

    public Certification(Employee employee, String type, LocalDate expiryDate) {
        this.employee = Objects.requireNonNull(employee);
        this.type = Objects.requireNonNull(type);
        this.expiryDate = Objects.requireNonNull(expiryDate);
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getType() {
        return type;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
