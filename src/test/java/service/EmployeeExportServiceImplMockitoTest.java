package service;

import model.Employee;
import model.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class EmployeeExportServiceImplMockitoTest {

    @InjectMocks
    private EmployeeExportServiceImpl exportService;

    @Mock
    private EmployeeProvider employeeProvider;
    @Spy
    private EmployeeFormatter employeeFormatter;
    @Spy
    private FileStorage fileStorage;

    @Test
    void shouldHandleIoExceptionWhenFailToWrite() throws IOException {
//        given
        List<Employee> employeeList = Collections.emptyList();
        String destination = "test.csv";
        String format = "CSV";
        boolean append = false;
        String formattedData = "header;data";

        Mockito.when(employeeProvider.findAll()).thenReturn(employeeList);
        Mockito.doReturn(formattedData).when(employeeFormatter).format(
                ArgumentMatchers.anyList(),
                ArgumentMatchers.anyString()
        );
        Mockito.doThrow(new IOException("Błąd zapisu")).when(fileStorage)
                .write(
                        ArgumentMatchers.eq(destination),
                        ArgumentMatchers.eq(formattedData),
                        ArgumentMatchers.eq(append)
                );

//        when
        exportService.export(destination, format, append);

//        then
        Mockito.verify(fileStorage, Mockito.times(1))
                .write(
                        ArgumentMatchers.eq(destination),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyBoolean()
                );
    }

    @Test
    void shouldSuccessfullyExportData() throws IOException {
//        given
        String destination = "test.csv";
        String format = "CSV";
        boolean append = false;
        String formattedData = "header;data";

        Mockito.when(employeeProvider.findAll()).thenReturn(Collections.emptyList());
        Mockito.doReturn(formattedData).when(employeeFormatter).format(
                ArgumentMatchers.anyList(),
                ArgumentMatchers.eq(format)
        );
//        when
        exportService.export(destination, format, append);

//        then
        Mockito.verify(employeeProvider, Mockito.times(1)).findAll();
        Mockito.verify(employeeFormatter, Mockito.times(1))
                .format(ArgumentMatchers.eq(
                                Collections.emptyList()),
                        ArgumentMatchers.eq(format)
                );
        Mockito.verify(fileStorage, Mockito.times(1)).write(
                ArgumentMatchers.eq(destination),
                ArgumentMatchers.eq(formattedData),
                ArgumentMatchers.eq(append)
        );
    }

    @Test
    void shouldWriteCorrectlyFormattedData() throws IOException {
//        given
        List<Employee> employeeList = List.of(employee("soemeamil@gmail.com"));
        String format = "JSON";

        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(employeeProvider.findAll()).thenReturn(employeeList);
        String expectedJsonData = "{\"employees\":[{\"firstName\":\"Alex\", \"email\":\"soemeamil@gmail.com\"}]}";
        Mockito.doReturn(expectedJsonData).when(employeeFormatter)
                .format(ArgumentMatchers.anyList(), ArgumentMatchers.eq(format));
//        when
        exportService.export("output.json", format, false);

//        then
        Mockito.verify(fileStorage, Mockito.times(1)).write(
                ArgumentMatchers.eq("output.json"),
                dataCaptor.capture(),
                ArgumentMatchers.eq(false)
        );

        String capturedData = dataCaptor.getValue();

        Assertions.assertTrue(capturedData.contains("Alex"));
        Assertions.assertTrue(capturedData.contains("soemeamil@gmail.com"));


        Mockito.verify(employeeFormatter, Mockito.times(1))
                .format(ArgumentMatchers.anyList(), ArgumentMatchers.eq(format));
    }

    private Employee employee(String email) {
        return new Employee("Alex", "Doe", email, "Example", Position.STAZYSTA, LocalDate.now());
    }
}