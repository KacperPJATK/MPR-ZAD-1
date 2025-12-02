package service;

import model.Employee;

public interface CommunicationService {
    void sendReminder(Employee employee, String message);
}
