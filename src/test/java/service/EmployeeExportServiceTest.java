package service;

import com.company.testing.doubles.EmployeeFormatterStub;
import com.company.testing.doubles.FileStorageMock;
import com.company.testing.doubles.FileStorageSpy;
import com.company.testing.doubles.InMemoryEmployeeProviderFake;
import model.Employee;
import model.Position;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class EmployeeExportServiceTest {

    // Sprawdza, czy eksport korzysta z podanego formatu, zwraca przygotowana tresc stuba i zapisuje ja raz do magazynu.
    @Test
    void shouldExportFormattedDataToFileStorage() {
        List<Employee> employees = List.of(
                employee("alice@example.com"),
                employee("bob@example.com")
        );
        InMemoryEmployeeProviderFake employeeProvider = new InMemoryEmployeeProviderFake(employees);
        EmployeeFormatterStub formatterStub = new EmployeeFormatterStub();
        FileStorageSpy fileStorageSpy = new FileStorageSpy();

        EmployeeExportService service = new EmployeeExportServiceImpl(
                employeeProvider,
                formatterStub,
                fileStorageSpy
        );

        service.export("report.csv", "CSV", false);

        assertAll(
                () -> assertThat(formatterStub.usedFormat()).isEqualTo("CSV"),
                () -> assertThat(fileStorageSpy.writes()).hasSize(1),
                () -> {
                    FileStorageSpy.Write write = fileStorageSpy.writes().getFirst();
                    assertAll(
                            () -> assertThat(write.path()).isEqualTo("report.csv"),
                            () -> assertThat(write.content()).isEqualTo("STUB_CONTENT"),
                            () -> assertThat(write.append()).isFalse()
                    );
                }
        );
    }

    // Sprawdza, czy zapis do pliku zostal wywolany wymagana liczbe razy zgodnie z oczekiwaniem mocka.
    @Test
    void shouldVerifyWriteCalledExpectedTimesWithMock() {
        List<Employee> employees = List.of(employee("alice@example.com"));
        InMemoryEmployeeProviderFake employeeProvider = new InMemoryEmployeeProviderFake(employees);
        EmployeeFormatterStub formatterStub = new EmployeeFormatterStub();
        FileStorageMock fileStorageMock = new FileStorageMock("out.json", false);

        EmployeeExportService service = new EmployeeExportServiceImpl(
                employeeProvider,
                formatterStub,
                fileStorageMock
        );

        service.export("out.json", "JSON", false);

        fileStorageMock.verify();
    }

    private Employee employee(String email) {
        return new Employee("Alex", "Doe", email, "Example", Position.STAZYSTA, LocalDate.now());
    }

}
