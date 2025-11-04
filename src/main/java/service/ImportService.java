package service;

import model.ImportSummary;

import java.nio.file.Path;

public interface ImportService {
    ImportSummary importData(Path path);
}
