package com.company.testing.doubles;

import model.Employee;
import service.CommunicationService;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")
/**
 * Szpieg CommunicationService zapamiętujący każdy wysłany reminder wraz z trśecią,
 * co pozwala testom sprawdzać komu i co zostało wysłane.
 */
public class CommunicationServiceSpy implements CommunicationService {
    private final List<SentReminder> sent = new ArrayList<>();

    @Override
    public void sendReminder(Employee employee, String message) {
        sent.add(new SentReminder(employee, message));
    }

    public List<SentReminder> sent() {
        return new ArrayList<>(sent);
    }

    public record SentReminder(Employee employee, String message) {
    }
}
