package service;

public interface EmployeeExportService {
    void export(String destinationPath, String format, boolean append);
}
