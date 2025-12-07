package com.company.testing.doubles;

import model.Employee;
import service.CommunicationService;

@SuppressWarnings("all")
/**
 * Mock CommunicationService pilnujący, by sendReminder został wywołany
 * dokładnie oczekiwaną liczbe razy; w przeciwnym razie zgłasza błąd.
 */
public class CommunicationServiceMock implements CommunicationService {
    private final int expectedCount;
    private int actualCount = 0;

    public CommunicationServiceMock(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void sendReminder(Employee employee, String message) {
        actualCount++;
        if (actualCount > expectedCount) {
            throw new AssertionError("sendReminder called more times than expected");
        }
    }

    public void verify() {
        if (actualCount != expectedCount) {
            String message = String.format("Expected %s calls but got %s", expectedCount, actualCount);
            throw new AssertionError(message);
        }
    }
}
