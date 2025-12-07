package com.company.testing.doubles;

import model.Employee;
import service.EmployeeFormatter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("all")
/**
 * Stub EmployeeFormatter zwracający przygotowane treści dla formatów i zapamietujący,
 * jaki format został użyty.
 */
public class EmployeeFormatterStub implements EmployeeFormatter {
    private final Map<String, String> predefined = new LinkedHashMap<>();
    private String usedFormat;

    public EmployeeFormatterStub() {
        predefined.put("CSV", "STUB_CONTENT");
        predefined.put("JSON", "{\"stub\":true}");
    }

    public void addFormat(String format, String value) {
        predefined.put(format, value);
    }

    @Override
    public String format(List<Employee> employees, String format) {
        usedFormat = format;
        return predefined.getOrDefault(format, "UNKNOWN");
    }

    public String usedFormat() {
        return usedFormat;
    }
}
