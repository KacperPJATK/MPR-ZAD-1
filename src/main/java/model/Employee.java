package model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Employee {
    private final String name;

    private final String surname;

    private String email;

    private String companyName;

    private Position position;

    private BigDecimal salary;

    public Employee(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Employee(
            String name, String surname, String email,
            String companyName, Position position
    ) {
        if (Objects.isNull(email) || email.isBlank()) {
            throw new IllegalArgumentException("Email nie może być pusty");
        } else {
            this.email = email.toLowerCase();
        }
        this.name = Objects.requireNonNull(name, "name nie może być null");
        this.surname = Objects.requireNonNull(surname, "surname nie może być null");
        this.companyName = Objects.requireNonNull(companyName, "companyName nie może być null");
        this.position = Objects.requireNonNull(position, "position nie może być null");
        this.salary = position.getSalary();
    }

    public static Employee emptyEmployee() {
        return new Employee("empty", "empty");
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Position getPosition() {
        return position;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", companyName='" + companyName + '\'' +
                ", position=" + position +
                ", salary=" + salary + "zł" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
