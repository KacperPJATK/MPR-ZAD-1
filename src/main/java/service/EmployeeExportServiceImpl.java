package service;

import model.Employee;
import repository.EmployeesRepository;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class EmployeeExportServiceImpl implements EmployeeExportService {
    private final EmployeeProvider employeeProvider;
    private final EmployeeFormatter employeeFormatter;
    private final FileStorage fileStorage;

    public EmployeeExportServiceImpl() {
        this.employeeProvider = EmployeesRepository::getEmployees;
        this.employeeFormatter = (employees, format) -> getFormatter(employees);
        this.fileStorage = EmployeeExportServiceImpl::getFileStorage;
    }

    EmployeeExportServiceImpl(EmployeeProvider employeeProvider,
                              EmployeeFormatter employeeFormatter,
                              FileStorage fileStorage
    ) {
        this.employeeProvider = employeeProvider;
        this.employeeFormatter = employeeFormatter;
        this.fileStorage = fileStorage;
    }

    private static void getFileStorage(String path, String content, boolean append) {
        Path target = Path.of(path);
        try (Writer writer = append
                ? Files.newBufferedWriter(target, StandardOpenOption.CREATE, StandardOpenOption.APPEND)
                : Files.newBufferedWriter(target)
        ) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormatter(List<Employee> employees) {
        StringBuilder builder = new StringBuilder();
        for (Employee employee : employees) {
            builder.append(employee.toString()).append(System.lineSeparator());
        }
        return builder.toString();
    }

    @Override
    public void export(String destinationPath, String format, boolean append) {
        List<Employee> employees = employeeProvider.findAll();
        String formatted = employeeFormatter.format(employees, format);
        fileStorage.write(destinationPath, formatted, append);
    }
}
