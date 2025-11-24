package model;

import java.util.List;
import java.util.Map;

public class DemographicReport {
    private final String companyName;

    private final Map<Long, List<Employee>> tenureMap;

    public DemographicReport(String companyName, Map<Long, List<Employee>> tenureMap) {
        this.companyName = companyName;
        this.tenureMap = tenureMap;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Map<Long, List<Employee>> getTenureMap() {
        return tenureMap;
    }
}
