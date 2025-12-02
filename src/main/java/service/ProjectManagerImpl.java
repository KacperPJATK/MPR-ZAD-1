package service;

import exception.TeamException;
import model.Employee;
import model.Position;
import repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectManagerImpl implements ProjectManager {
    private static void validateTeamComposition(List<Employee> assignedToProject) {
        Map<Position, Long> collect = assignedToProject.stream()
                .collect(Collectors.groupingBy(
                        Employee::getPosition,
                        Collectors.counting()
                ));

        long teamSize = collect.values().stream().mapToLong(value -> value).sum();

        if (teamSize > 9) {
            String message = String.format("Team Exceeded max size: %s", teamSize);
            throw new TeamException(message);
        }

        long stazysci = collect.getOrDefault(Position.STAZYSTA, 0L);
        long programisci = collect.getOrDefault(Position.PROGRAMISTA, 0L);
        long managerowie = collect.getOrDefault(Position.MANAGER, 0L);
        long wiceprezesi = collect.getOrDefault(Position.WICEPREZES, 0L);
        long prezesi = collect.getOrDefault(Position.PREZES, 0L);

        if (stazysci < 2) {
            String message = String.format("Not Enough STAZYSTA: %s", stazysci);
            throw new TeamException(message);
        }

        if (programisci < 2) {
            String message = String.format("Not Enough PROGRAMISTA: %s", programisci);
            throw new TeamException(message);
        }

        if (managerowie < 1) {
            String message = String.format("Not Enough MANAGER: %s", managerowie);
            throw new TeamException(message);
        }

        if (wiceprezesi > 1) {
            String message = String.format("More than one WICEPREZES: %s", wiceprezesi);
            throw new TeamException(message);
        }

        if (prezesi > 1) {
            String message = String.format("More than one PREZES: %s", prezesi);
            throw new TeamException(message);
        }
    }

    @Override
    public boolean createTeam(String teamName, List<Employee> assignedToProject) {

        if (assignedToProject.isEmpty()) {
            return false;
        }

        validateTeamComposition(assignedToProject);

        if (ProjectRepository.containsTeam(teamName)) {
            String message = String.format("Team with name: %s already exists", teamName);
            throw new TeamException(message);
        }

        ProjectRepository.addProject(teamName, assignedToProject);
        return true;
    }

    @Override
    public boolean moveEmployee(String email, String from, String to) {

        if (!ProjectRepository.containsTeam(from)) {
            String message = String.format("Team with name: %s does not exist", from);
            throw new TeamException(message);
        }

        if (!ProjectRepository.containsTeam(to)) {
            String message = String.format("Team with name: %s does not exist", to);
            throw new TeamException(message);
        }

        List<Employee> fromTeam = ProjectRepository.getTeam(from);
        List<Employee> toTeam = ProjectRepository.getTeam(to);


        Employee employeeToMove = fromTeam.stream()
                .filter(e -> e.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        if (employeeToMove == null) {
            String message = String.format("Employee with email: %s not found in team: %s", email, from);
            throw new TeamException(message);
        }

        List<Employee> fromAfterMove = new ArrayList<>(fromTeam);
        List<Employee> toAfterMove = new ArrayList<>(toTeam);

        fromAfterMove.remove(employeeToMove);
        toAfterMove.add(employeeToMove);

        validateTeamComposition(fromAfterMove);
        validateTeamComposition(toAfterMove);

        ProjectRepository.moveBetween(email, from, to);

        return true;
    }
}
