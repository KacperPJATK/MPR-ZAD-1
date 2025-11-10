package service;

import exception.ApiException;
import model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static model.Position.PROGRAMISTA;

class ApiServiceTest {

    private ApiService apiService;

    private List<Employee> expected;

    @BeforeEach
    void setUp() {
        apiService = new ApiService();
        expected = List.of(
                new Employee(
                        "Leanne", "Graham", "sincere@april.biz",
                        "Romaguera-Crona", PROGRAMISTA
                ),
                new Employee(
                        "Ervin", "Howell", "shanna@melissa.tv",
                        "Deckow-Crist", PROGRAMISTA
                ),
                new Employee(
                        "Clementine", "Bauch", "nathan@yesenia.net",
                        "Romaguera-Jacobson", PROGRAMISTA
                ),
                new Employee(
                        "Patricia", "Lebsack", "julianne.oconner@kory.org",
                        "Robel-Corkery", PROGRAMISTA
                ),
                new Employee(
                        "Chelsey", "Dietrich", "lucio_hettinger@annie.ca"
                        , "Keebler LLC", PROGRAMISTA
                ),
                new Employee(
                        "Mrs.", "Dennis", "karley_dach@jasper.info",
                        "Considine-Lockman", PROGRAMISTA
                ),
                new Employee(
                        "Kurtis", "Weissnat", "telly.hoeger@billy.biz",
                        "Johns Group", PROGRAMISTA
                ),
                new Employee(
                        "Nicholas", "Runolfsdottir", "sherwood@rosamond.me",
                        "Abernathy Group", PROGRAMISTA
                ),
                new Employee(
                        "Glenna", "Reichert", "chaim_mcdermott@dana.io",
                        "Yost and Sons", PROGRAMISTA
                ),
                new Employee(
                        "Clementina", "DuBuque", "rey.padberg@karina.biz",
                        "Hoeger LLC", PROGRAMISTA
                )
        );
    }

    @Test
    void fetchEmployeesFromApi() {
//        given
        String uri = "https://jsonplaceholder.typicode.com/users";
//        when
        List<Employee> result = apiService.fetchEmployeesFromApi(uri);
//        then
        Assertions.assertTrue(expected.containsAll(result));
    }

    @Test
    void shouldThrowWhenProvidingWrongUri() {
//        given
        String uri = "https://jsonplaceholder.typicode.com/users1212";
//        when, then
        Assertions.assertThrows(ApiException.class, () -> apiService.fetchEmployeesFromApi(uri));
    }
}