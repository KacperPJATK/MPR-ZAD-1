package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exception.ApiException;
import model.Employee;
import model.Position;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApiService {
    public List<Employee> fetchEmployeesFromApi(String uri) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonArray users = JsonParser.parseString(response.body()).getAsJsonArray();
                List<Employee> employees = new ArrayList<>();

                for (JsonElement element : users) {
                    JsonObject user = element.getAsJsonObject();
                    String[] fullName = user.get("name").getAsString().trim().split(" ");
                    String email = user.get("email").getAsString().trim();
                    String companyName = user.getAsJsonObject("company")
                            .get("name")
                            .getAsString().trim();

                    employees.add(
                            new Employee(fullName[0], fullName[1], email,
                                    companyName, Position.PROGRAMISTA,
                                    LocalDate.now()
                            )
                    );
                }
                return employees;

            } else {
                String message = String.format("Błąd HTTP: %s", response.statusCode());
                throw new ApiException(message);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new ApiException(e.getMessage());
        }


    }
}
