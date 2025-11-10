package service;

import model.ImportSummary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.EmployeesRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class CSVImportServiceTest {

    private Path path;
    private ImportService importService;

    @AfterAll
    static void clearRepository() {
        EmployeesRepository.clearForTest();
    }

    @BeforeEach
    void setUp() {
        path = Paths.get("src/test/resources/employees.csv");
        importService = new CSVImportService();
    }

    @Test
    void importData() {
//        given
        ImportSummary expected = new ImportSummary(
                8, List.of(
                "Failed; Pozycja: PROGRA__MISTA nie istnieje, linia: 7",
                "Failed; Wypłata: -18000 jest ujemna, linia: 9"
        ));
//        when
        ImportSummary result = importService.importData(path);
//        then
        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        expected.getNumberOfEmployeesImported(), result.getNumberOfEmployeesImported()
                ),
                () -> Assertions.assertTrue(expected.getErrorList().containsAll(result.getErrorList()))
        );
    }
}