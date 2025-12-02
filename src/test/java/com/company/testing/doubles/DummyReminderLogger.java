package com.company.testing.doubles;

import service.ReminderLogger;
@SuppressWarnings("all")
/**
 * Dummy ReminderLogger, ktory ignoruje logi, aby testy nie zalezaly od rzeczywistego logowania.
 */
public class DummyReminderLogger implements ReminderLogger {
    @Override
    public void log(String message) {
    }
}
