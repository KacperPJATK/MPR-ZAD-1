package com.company.testing.doubles;

import java.util.Objects;

@SuppressWarnings("all")
/**
 * Dummy obiekt konfiguracyjny sluzacy wylacznie do zapelnienia zaleznosci w testach.
 */
public final class DummyConfig {
    private final String value;

    public DummyConfig(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DummyConfig) obj;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "DummyConfig[" +
                "value=" + value + ']';
    }

}
