package ru.savin.minicrm.service;

import org.springframework.data.domain.Page;
import ru.savin.minicrm.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();

    Optional<Employee> findById(Long id);

    Employee save(Employee employee);

    void delete(Long id);

    List<Employee> searchBy(String keyword);

    Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

}
