package service;

import exception.InvalidDataException;
import model.Employee;
import model.ImportSummary;
import model.Pair;
import model.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVImportService implements ImportService {
    @Override
    public ImportSummary importData(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {

            AtomicInteger counter = new AtomicInteger();
            List<Pair<String, Employee>> pairs = reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(line -> new Pair<>(counter.incrementAndGet(), line))
                    .skip(1)
                    .map(this::getEmployeePair)
                    .toList();

            addToEmployees(pairs);

            return getImportSummary(pairs);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToEmployees(List<Pair<String, Employee>> pairs) {
        List<Employee> employees = pairs.stream()
                .filter(pair -> pair.getKey().contains("Success"))
                .map(Pair::getValue)
                .toList();
        EmployeesManager employeesManager = new EmployeesManagerImpl();
        for (Employee employee : employees) {
            employeesManager.addEmployee(employee);
        }

    }

    private ImportSummary getImportSummary(List<Pair<String, Employee>> pairs) {
        List<String> errorList = new ArrayList<>();
        int counter = 0;

        for (Pair<String, Employee> pair : pairs) {
            if (pair.getKey().contains("Failed")) {
                errorList.add(pair.getKey());
            } else {
                counter++;
            }
        }
        return new ImportSummary(counter, errorList);
    }

    private Pair<String, Employee> getEmployeePair(Pair<Integer, String> pair) {
        String[] employeeData = pair.getValue().split(",");
        Integer lineNumber = pair.getKey();
        String name = employeeData[0];
        String lastName = employeeData[1];
        String email = employeeData[2];
        String company = employeeData[3];
        String position = employeeData[4];
        String salary = employeeData[5];

        boolean isPositionCorrect = true;

        try {
            Position.valueOf(position.toUpperCase());
        } catch (IllegalArgumentException e) {
            isPositionCorrect = false;
        }

        try {
            if (!isPositionCorrect) {
                String message = String.format("Failed; Pozycja: %s nie istnieje, linia: %s", position, lineNumber);
                throw new InvalidDataException(message);
            }
        } catch (InvalidDataException exception) {
            return new Pair<>(exception.getMessage(), Employee.emptyEmployee());
        }


        try {
            if (Integer.parseInt(salary) < 0) {
                String message = String.format("Failed; Wypłata: %s jest ujemna, linia: %s", salary, lineNumber);
                return new Pair<>(message, Employee.emptyEmployee());
            }
        } catch (InvalidDataException exception) {
            return new Pair<>(exception.getMessage(), Employee.emptyEmployee());
        }


        String message = String.format("Success; linia: %s", lineNumber);
        return new Pair<>(message,
                new Employee(
                        name, lastName, email, company,
                        Position.valueOf(position.toUpperCase())
                )
        );
    }
}
