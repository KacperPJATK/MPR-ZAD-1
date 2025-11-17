package service;

import exception.PromotionException;
import exception.RaiseException;
import model.Employee;
import model.Position;
import repository.EmployeesRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class RaiseAndPromotionServiceImpl implements RaiseAndPromotionService {
    @Override
    public void promote(String email, Position position) {
        if (Objects.isNull(email) || Objects.isNull(position)) {
            String message = String.format("Illegal argument: %s, %s", email, position);
            throw new IllegalArgumentException(message);
        }

        if (!EmployeesRepository.containsEmail(email)) {
            String message = String.format("There is no employee with email: %s", email);
            throw new PromotionException(message);
        }

        Employee employee = EmployeesRepository.getEmployee(email);

        if (
                position.getHierarchyLevel() >= employee.getPosition().getHierarchyLevel() ||
                        position.getHierarchyLevel() + 1 != employee.getPosition().getHierarchyLevel()
        ) {
            String message = String.format(
                    "You cant demote/jump over two positions at once: old: %s, new: %s",
                    position, employee.getPosition()
            );
            throw new PromotionException(message);
        }

        employee.setPosition(position);
        employee.setSalary(position.getSalary());
    }

    @Override
    public void giveRaise(String email, int percent) {
        if (Objects.isNull(email) || percent <= 0) {
            String message = String.format("Illegal argument: %s, %s", email, percent);
            throw new IllegalArgumentException(message);
        }

        if (!EmployeesRepository.containsEmail(email)) {
            String message = String.format("There is no employee with email: %s", email);
            throw new RaiseException(message);
        }
        Employee employee = EmployeesRepository.getEmployee(email);

        BigDecimal newSalary = employee.getSalary()
                .add(employee.getSalary()
                        .multiply(BigDecimal.valueOf(percent))
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                );


        if (newSalary.compareTo(Position.getNextSalary(employee.getPosition())) > 0) {
            String message = String.format(
                    "Salary exceeded: new: %s limit: %s",
                    newSalary, Position.getNextSalary(employee.getPosition())
            );
            throw new RaiseException(message);
        }

        employee.setSalary(newSalary);
    }
}
