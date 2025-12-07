package com.company.testing.doubles;

import service.ReminderLogger;
@SuppressWarnings("all")
/**
 * Dummy ReminderLogger, który ignoruje logi, aby testy nie zależały od rzeczywistego logowania.
 */
public class DummyReminderLogger implements ReminderLogger {
    @Override
    public void log(String message) {
    }
}
