package model;

import java.math.BigDecimal;

public enum Position {
    PREZES(BigDecimal.valueOf(25000), 1),
    WICEPREZES(BigDecimal.valueOf(18000), 2),
    MANAGER(BigDecimal.valueOf(12000), 3),
    PROGRAMISTA(BigDecimal.valueOf(8000), 4),
    STAZYSTA(BigDecimal.valueOf(3000), 5);

    private final BigDecimal salary;
    private final int hierarchyLevel;

    Position(BigDecimal salary, int hierarchyLevel) {
        this.salary = salary;
        this.hierarchyLevel = hierarchyLevel;
    }

    public static BigDecimal getNextSalary(Position position) {
        switch (position) {
            case PREZES -> {
                return BigDecimal.valueOf(Integer.MAX_VALUE);
            }
            case WICEPREZES -> {
                return PREZES.getSalary();
            }
            case MANAGER -> {
                return WICEPREZES.getSalary();
            }
            case PROGRAMISTA -> {
                return MANAGER.getSalary();
            }
            case STAZYSTA -> {
                return PROGRAMISTA.getSalary();
            }
            default -> {
                return BigDecimal.ZERO;
            }
        }

    }

    public BigDecimal getSalary() {
        return salary;
    }

    public int getHierarchyLevel() {
        return hierarchyLevel;
    }
}
