package ru.savin.minicrm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.savin.minicrm.dao.EmployeeRepository;
import ru.savin.minicrm.entity.Employee;
import ru.savin.minicrm.exception.EmployeeNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class)
//This extension is the JUnit Jupiter equivalent of our JUnit4 MockitoJUnitRunner.
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    private static final String NAME_FOR_SEARCH = "Vasya";
    private static final Long EMPLOYEE_ID = 5L;
    private static final String EMPLOYEE_FIRST_NAME = "Andrey";
    private static final String EMPLOYEE_LAST_NAME = "Savin";
    private static final String EMPLOYEE_EMAIL = "sava@mail.ru";
    private static final String EMPLOYEE_PHOTO = "sava.jpg";

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void findAllTest() {
        Employee employee = createEmployeeObject(EMPLOYEE_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                EMPLOYEE_EMAIL, EMPLOYEE_PHOTO);

        List<Employee> actualEmployees = Arrays.asList(employee);

        doReturn(actualEmployees).when(employeeRepository).findAll();

        List<Employee> expectedEmployees = employeeService.findAll();

        assertThat(expectedEmployees).isEqualTo(actualEmployees);
    }

    @Test
    public void findByIdTest() {
        Optional<Employee> actualEmployee = Optional.of(createEmployeeObject(EMPLOYEE_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                EMPLOYEE_EMAIL, EMPLOYEE_PHOTO));

        doReturn(actualEmployee).when(employeeRepository).findById(EMPLOYEE_ID);

        Optional<Employee> expectedEmployee = employeeService.findById(EMPLOYEE_ID);

        assertThat(expectedEmployee).isEqualTo(actualEmployee);
    }

    @Test
    void saveTest() {
        Employee employee = createEmployeeObject(EMPLOYEE_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                EMPLOYEE_EMAIL, EMPLOYEE_PHOTO);

        doReturn(employee).when(employeeRepository).save(employee);

        Employee savedEmployee = employeeService.save(employee);

        assertEquals(savedEmployee, employee);
        verify(employeeRepository).save(any(Employee.class));

    }

    @Test
    void deleteTest() {
        Employee employeeToDelete = createEmployeeObject(EMPLOYEE_ID, EMPLOYEE_FIRST_NAME, EMPLOYEE_LAST_NAME,
                EMPLOYEE_EMAIL, EMPLOYEE_PHOTO);

        doReturn(Optional.of(employeeToDelete)).when(employeeRepository).findById(EMPLOYEE_ID);

        employeeService.delete(EMPLOYEE_ID);

        verify(employeeRepository, times(1)).deleteById(EMPLOYEE_ID);
    }

    @Test
    void deleteWhenEmployeeIsNotFoundTest() {
        doReturn(Optional.empty()).when(employeeRepository).findById(EMPLOYEE_ID);

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.delete(EMPLOYEE_ID);
        });

        verify(employeeRepository, never()).deleteById(EMPLOYEE_ID);
        verifyNoMoreInteractions(employeeRepository);
    }


    @Test
    void searchByTest() {
        employeeService.searchBy(NAME_FOR_SEARCH);

        verify(employeeRepository, times(1)).findByFirstNameContainsOrLastNameContainsAllIgnoreCase(NAME_FOR_SEARCH,
                NAME_FOR_SEARCH);
        verifyNoMoreInteractions(employeeRepository);
    }

    public Employee createEmployeeObject(Long id, String firstName, String lastName, String email, String photo) {
        Employee employee = new Employee();

        employee.setId(id);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPhoto(photo);

        return employee;
    }
}