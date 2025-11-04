package model;

import java.math.BigDecimal;

public final class CompanyStatistics {
    private final Integer numberOfEmployeesInCompany;
    private final BigDecimal averageSalary;
    private final String bestEarningEmployeeCredentials;

    public CompanyStatistics(Integer numberOfEmployeesInCompany, BigDecimal averageSalary, String bestEarningEmployeeCredentials) {
        this.numberOfEmployeesInCompany = numberOfEmployeesInCompany;
        this.averageSalary = averageSalary;
        this.bestEarningEmployeeCredentials = bestEarningEmployeeCredentials;
    }

    public Integer getNumberOfEmployeesInCompany() {
        return numberOfEmployeesInCompany;
    }

    public BigDecimal getAverageSalary() {
        return averageSalary;
    }

    public String getBestEarningEmployeeCredentials() {
        return bestEarningEmployeeCredentials;
    }

    @Override
    public String toString() {
        return "CompanyStatistics{" +
                "numberOfEmployeesInCompany=" + numberOfEmployeesInCompany +
                ", averageSalary=" + averageSalary +
                ", bestEarningEmployeeCredentials='" + bestEarningEmployeeCredentials + '\'' +
                '}';
    }
}
