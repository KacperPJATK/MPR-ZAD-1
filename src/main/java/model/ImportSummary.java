package model;

import java.util.List;

public final class ImportSummary {
    private final Integer numberOfEmployeesImported;

    private final List<String> errorList;

    public ImportSummary(Integer numberOfEmployeesImported, List<String> errorList) {
        this.numberOfEmployeesImported = numberOfEmployeesImported;
        this.errorList = errorList;
    }

    public Integer getNumberOfEmployeesImported() {
        return numberOfEmployeesImported;
    }

    public List<String> getErrorList() {
        return errorList;
    }
}
