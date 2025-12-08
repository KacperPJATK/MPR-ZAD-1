package service;

import java.io.IOException;

public interface EmployeeExportService {
    void export(String destinationPath, String format, boolean append) throws IOException;
}
