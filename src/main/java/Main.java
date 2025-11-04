import model.Employee;
import model.Position;
import service.*;

import java.math.BigDecimal;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        EmployeesManager employeesManager = new EmployeesManagerImpl();
        AnalyticalOperationManager analytical = new AnalyticalOperationManagerImpl();
        FinancialOperationManager financial = new FinancialOperationManagerImpl();

        ImportService importService = new CSVImportService();
        importService.importData(Paths.get("src/main/resources/employees.csv"));

        employeesManager.addEmployee(new Employee(
                "Anna", "Nowak", "anna.nowak@techcorp.com",
                "TechCorp", Position.PROGRAMISTA)
        );

        employeesManager.addEmployee(new Employee(
                "Bartek", "Kowalski", "bartek.kowalski@techcorp.com",
                "TechCorp", Position.MANAGER)
        );

        employeesManager.addEmployee(new Employee(
                "Celina", "Zielińska", "celina.zielinska@techcorp.com",
                "TechCorp", Position.STAZYSTA)
        );

        employeesManager.addEmployee(new Employee(
                "Dawid", "Adamski", "dawid.adamski@outsourcing.com",
                "Outsourcing", Position.PROGRAMISTA)
        );

        employeesManager.addEmployee(new Employee(
                "Ewa", "Prezes", "ewa.prezes@techcorp.com",
                "TechCorp", Position.PREZES)
        );

        System.out.println("\u001B[32m=== Wszyscy pracownicy ===\u001B[0m");
        employeesManager.displayEmployees();

        System.out.println("\u001B[32m\n=== Pracownicy TechCorp ===\u001B[0m");
        analytical.findEmployeesByCompany("TechCorp").forEach(System.out::println);

        System.out.println("\u001B[32m\n=== Posortowani alfabetycznie (nazwisko, imię) ===\u001B[0m");
        analytical.getEmployeesAlphabetically()
                .forEach(e -> System.out.println(e.getSurname() + ", " + e.getName()));

        System.out.println("\u001B[32m\n=== Grupowanie po stanowisku ===\u001B[0m");
        analytical.getEmployeesGroupedByPosition().forEach(
                (pos, list) -> System.out.println(pos + ": " + list)
        );

        System.out.println("\u001B[32m\n=== Liczba pracowników na stanowisku ===\u001B[0m");
        System.out.println(analytical.getNumberOfEmployeesPerPosition());

        System.out.println("\u001B[32m\n=== Średnia pensja w organizacji ===\u001B[0m");
        BigDecimal avg = financial.getAverageSalary();
        System.out.println(avg + " zł");

        System.out.println("\u001B[32m\n=== Najlepiej opłacany pracownik ===\u001B[0m");
        System.out.println(financial.getTheBestPaidEmployee());

        try {
            System.out.println("\u001B[32m\n=== Próba dodania pracownika o takim samym mailu ===\u001B[0m");
            employeesManager.addEmployee(new Employee(
                    "Anna", "Nowak", "anna.nowak@techcorp.com",
                    "TechCorp", Position.PROGRAMISTA)
            );
        } catch (IllegalArgumentException e) {
            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
//            System.err.println(e.getMessage()); -- można też tak ale
//            ze względu na sposób drukowania użyłem wersji powyżej
        }


    }
}
