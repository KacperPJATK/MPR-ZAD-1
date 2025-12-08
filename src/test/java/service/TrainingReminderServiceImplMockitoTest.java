package service;

import model.Certification;
import model.Employee;
import model.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CertificateRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TrainingReminderServiceImplMockitoTest {

    @InjectMocks
    private TrainingReminderServiceImpl trainingReminderService;
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private CommunicationService communicationService;

    @Test
    void checkIfMessageWasSentWithCorrectEmail() {
//        given
        LocalDate today = LocalDate.of(2025, 12, 14);
        LocalDate until = today.plusDays(30);
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        List<Certification> expiredList = prepareExpiredList();
//        when
        Mockito.when(certificateRepository.findExpiringBetween(today, until))
                .thenReturn(expiredList);
        trainingReminderService.sendReminders(today);
//        then
        Mockito.verify(communicationService, Mockito.times(2))
                .sendReminder(employeeCaptor.capture(), ArgumentMatchers.anyString());

        List<Employee> values = employeeCaptor.getAllValues();

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, values.size()),
                () -> Assertions.assertEquals(
                        "someemail@gmail.com",
                        values.getFirst().getEmail()
                ),
                () -> Assertions.assertEquals(
                        "someotheremail@gmail.com",
                        values.getLast().getEmail()
                )
        );

    }

    @Test
    void checksIfReminderWasSentWithCorrectMessage() {
//        given
        LocalDate today = LocalDate.of(2025, 12, 14);
        LocalDate until = today.plusDays(30);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        List<Certification> expiredList = prepareExpiredList();
        String message1 = String.format("Przypomnienie: %s wygasa %s.",
                expiredList.getFirst().getType(),
                expiredList.getFirst().getExpiryDate()
        );
        String message2 = String.format("Przypomnienie: %s wygasa %s.",
                expiredList.getLast().getType(),
                expiredList.getLast().getExpiryDate()
        );
//        when
        Mockito.when(certificateRepository.findExpiringBetween(today, until))
                .thenReturn(expiredList);
        trainingReminderService.sendReminders(today);
//        then
        Mockito.verify(communicationService, Mockito.times(2))
                .sendReminder(ArgumentMatchers.isA(Employee.class), messageCaptor.capture());

        List<String> messages = messageCaptor.getAllValues();

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, messages.size()),
                () -> Assertions.assertEquals(message1, messages.getFirst()),
                () -> Assertions.assertEquals(message2, messages.getLast())
        );

    }

    @Test
    void shouldNotSentReminderWhereThereAreNoExpiredCertificates() {
//        given
        LocalDate today = LocalDate.of(2025, 12, 14);
        LocalDate until = today.plusDays(30);
//        when
        Mockito.when(certificateRepository.findExpiringBetween(today, until))
                .thenReturn(Collections.emptyList());
        trainingReminderService.sendReminders(today);
//        then
        Mockito.verify(communicationService, Mockito.never())
                .sendReminder(ArgumentMatchers.isA(Employee.class), ArgumentMatchers.anyString());
    }


    private List<Certification> prepareExpiredList() {
        return List.of(
                new Certification(
                        employee("someemail@gmail.com", Position.PROGRAMISTA),
                        "Jakieś tam ;)",
                        LocalDate.of(2026, 1, 20)
                ),
                new Certification(
                        employee("someotheremail@gmail.com", Position.STAZYSTA),
                        "Jakieś inne  ;)",
                        LocalDate.of(2026, 2, 20)
                )
        );
    }

    private Employee employee(String email, Position position) {
        return new Employee("Alex", "Doe", email, "Example", position, LocalDate.now());
    }
}