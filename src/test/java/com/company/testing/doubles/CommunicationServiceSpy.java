package com.company.testing.doubles;

import model.Employee;
import service.CommunicationService;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("all")
/**
 * Szpieg CommunicationService zapamietujacy kazdy wyslany reminder wraz z trescia,
 * co pozwala testom sprawdzac komu i co zostalo wyslane.
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
