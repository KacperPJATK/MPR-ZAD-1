package service;

import com.company.testing.doubles.CertificateRepositoryStub;
import com.company.testing.doubles.CommunicationServiceMock;
import com.company.testing.doubles.CommunicationServiceSpy;
import com.company.testing.doubles.DummyReminderLogger;
import model.Certification;
import model.Employee;
import model.Position;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TrainingReminderServiceTest {

    // Sprawdza, czy wysylane jest przypomnienie tylko dla certyfikatow wygasajacych w podanym oknie z poprawnymi danymi.
    @Test
    void shouldSendRemindersForCertificatesExpiringSoon() {
//        given
        Employee anna = employee("anna@example.com");
        Employee bob = employee("bob@example.com");

        CertificateRepositoryStub repositoryStub = new CertificateRepositoryStub(List.of(
                new Certification(anna, "BHP", LocalDate.of(2024, 1, 15)),
                new Certification(bob, "RODO", LocalDate.of(2024, 3, 1))
        ));
        CommunicationServiceSpy communicationSpy = new CommunicationServiceSpy();
        TrainingReminderService service = new TrainingReminderServiceImpl(
                repositoryStub,
                communicationSpy,
                new DummyReminderLogger()
        );
//        when
        service.sendReminders(LocalDate.of(2024, 1, 1));
//        then
        assertAll(
                () -> assertThat(communicationSpy.sent()).hasSize(1),
                () -> {
                    CommunicationServiceSpy.SentReminder sent = communicationSpy.sent().getFirst();
                    assertAll(
                            () -> assertThat(sent.employee().getEmail()).isEqualTo("anna@example.com"),
                            () -> assertThat(sent.message()).contains("BHP").contains("2024-01-15")
                    );
                }
        );
    }

    // Sprawdza, czy liczba wyslanych przypomnien zgadza sie z oczekiwaniem mocka komunikacyjnego.
    @Test
    void shouldMatchExpectedSendCountWithMock() {
//        given
        Employee anna = employee("anna@example.com");
        Employee bob = employee("bob@example.com");

        CertificateRepositoryStub repositoryStub = new CertificateRepositoryStub(List.of(
                new Certification(anna, "BHP", LocalDate.of(2024, 1, 10)),
                new Certification(bob, "PIERWSZA_POMOC", LocalDate.of(2024, 1, 12))
        ));
        CommunicationServiceMock communicationMock = new CommunicationServiceMock(2);
        TrainingReminderService service = new TrainingReminderServiceImpl(
                repositoryStub,
                communicationMock,
                new DummyReminderLogger()
        );
//        when

        service.sendReminders(LocalDate.of(2024, 1, 1));

//        then
        communicationMock.verify();
    }

    private Employee employee(String email) {
        return new Employee("Alex", "Doe", email, "Example", Position.STAZYSTA, LocalDate.now());
    }
}
