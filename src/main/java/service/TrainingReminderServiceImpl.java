package service;

import model.Certification;
import model.Employee;
import repository.CertificateRepository;

import java.time.LocalDate;
import java.util.List;

public class TrainingReminderServiceImpl implements TrainingReminderService {
    private final CertificateRepository certificateRepository;
    private final CommunicationService communicationService;
    private final ReminderLogger reminderLogger;

    public TrainingReminderServiceImpl(CertificateRepository certificateRepository,
                                       CommunicationService communicationService,
                                       ReminderLogger reminderLogger
    ) {
        this.certificateRepository = certificateRepository;
        this.communicationService = communicationService;
        this.reminderLogger = reminderLogger;
    }

    @Override
    public void sendReminders(LocalDate today) {
        LocalDate until = today.plusDays(30);
        List<Certification> expiring = certificateRepository.findExpiringBetween(today, until);
        for (Certification certification : expiring) {
            Employee employee = certification.getEmployee();
            String message = String.format("Przypomnienie: %s wygasa %s.",
                    certification.getType(),
                    certification.getExpiryDate()
            );
            communicationService.sendReminder(employee, message);
        }
    }
}
