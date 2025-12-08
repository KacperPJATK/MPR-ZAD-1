package service;

import model.Employee;

import java.io.IOException;
import java.util.List;

public class EmployeeExportServiceImpl implements EmployeeExportService {
    private final EmployeeProvider employeeProvider;
    private final EmployeeFormatter employeeFormatter;
    private final FileStorage fileStorage;

    EmployeeExportServiceImpl(EmployeeProvider employeeProvider,
                              EmployeeFormatter employeeFormatter,
                              FileStorage fileStorage
    ) {
        this.employeeProvider = employeeProvider;
        this.employeeFormatter = employeeFormatter;
        this.fileStorage = fileStorage;
    }


    @Override
    public void export(String destinationPath, String format, boolean append) {
        List<Employee> employees = employeeProvider.findAll();
        String formatted = employeeFormatter.format(employees, format);

        try {
            fileStorage.write(destinationPath, formatted, append);
        } catch (IOException e) {
            System.err.println("Błąd zapisu danych: " + e.getMessage());
        }
    }
}