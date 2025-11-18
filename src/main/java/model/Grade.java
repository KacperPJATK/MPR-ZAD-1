package model;

import java.math.BigDecimal;

public enum Grade {
    FIVE(BigDecimal.valueOf(5)),
    FOUR(BigDecimal.valueOf(4)),
    THREE(BigDecimal.valueOf(3)),
    TWO(BigDecimal.TWO),
    ONE(BigDecimal.ONE);
    private final BigDecimal value;

    Grade(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
