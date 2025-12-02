package service;

import java.time.LocalDate;

public interface TrainingReminderService {
    void sendReminders(LocalDate today);
}
