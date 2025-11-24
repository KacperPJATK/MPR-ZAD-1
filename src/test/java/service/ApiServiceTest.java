package service;

import exception.ApiException;
import model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
                        "Romaguera-Crona", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Ervin", "Howell", "shanna@melissa.tv",
                        "Deckow-Crist", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Clementine", "Bauch", "nathan@yesenia.net",
                        "Romaguera-Jacobson", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Patricia", "Lebsack", "julianne.oconner@kory.org",
                        "Robel-Corkery", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Chelsey", "Dietrich", "lucio_hettinger@annie.ca"
                        , "Keebler LLC", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Mrs.", "Dennis", "karley_dach@jasper.info",
                        "Considine-Lockman", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Kurtis", "Weissnat", "telly.hoeger@billy.biz",
                        "Johns Group", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Nicholas", "Runolfsdottir", "sherwood@rosamond.me",
                        "Abernathy Group", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Glenna", "Reichert", "chaim_mcdermott@dana.io",
                        "Yost and Sons", PROGRAMISTA, LocalDate.now()
                ),
                new Employee(
                        "Clementina", "DuBuque", "rey.padberg@karina.biz",
                        "Hoeger LLC", PROGRAMISTA, LocalDate.now()
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