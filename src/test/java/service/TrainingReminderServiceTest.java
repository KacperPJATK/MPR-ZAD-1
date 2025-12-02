package service;

import model.Certification;
import model.Employee;
import model.Position;
import org.junit.jupiter.api.Test;
import repository.CertificateRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TrainingReminderServiceTest {

    @Test
    void shouldSendRemindersForCertificatesExpiringSoon() {
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

        service.sendReminders(LocalDate.of(2024, 1, 1));

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

    @Test
    void shouldMatchExpectedSendCountWithMock() {
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

        service.sendReminders(LocalDate.of(2024, 1, 1));

        communicationMock.verify();
    }

    private Employee employee(String email) {
        return new Employee("Alex", "Doe", email, "Example", Position.STAZYSTA, LocalDate.now());
    }

    private static final class CertificateRepositoryStub implements CertificateRepository {
        private final List<Certification> certifications;

        CertificateRepositoryStub(List<Certification> certifications) {
            this.certifications = certifications;
        }

        @Override
        public List<Certification> findExpiringBetween(LocalDate from, LocalDate to) {
            List<Certification> result = new ArrayList<>();
            for (Certification certification : certifications) {
                LocalDate expiry = certification.getExpiryDate();
                boolean withinRange = (expiry.isEqual(from) || expiry.isAfter(from))
                        && (expiry.isEqual(to) || expiry.isBefore(to));
                if (withinRange) {
                    result.add(certification);
                }
            }
            return result;
        }
    }

    private static final class CommunicationServiceSpy implements CommunicationService {
        private final List<SentReminder> sent = new ArrayList<>();

        @Override
        public void sendReminder(Employee employee, String message) {
            sent.add(new SentReminder(employee, message));
        }

        List<SentReminder> sent() {
            return new ArrayList<>(sent);
        }

        private record SentReminder(Employee employee, String message) {
        }
    }

    private static final class CommunicationServiceMock implements CommunicationService {
        private final int expectedCount;
        private int actualCount = 0;

        CommunicationServiceMock(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void sendReminder(Employee employee, String message) {
            actualCount++;
            if (actualCount > expectedCount) {
                throw new AssertionError("sendReminder called more times than expected");
            }
        }

        void verify() {
            if (actualCount != expectedCount) {
                String message = String.format("Expected %s calls but got %s", expectedCount, actualCount);
                throw new AssertionError(message);
            }
        }
    }

    private static final class DummyReminderLogger implements ReminderLogger {
        @Override
        public void log(String message) {
        }
    }
}
