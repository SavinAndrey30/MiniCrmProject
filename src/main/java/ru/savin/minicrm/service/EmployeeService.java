package ru.savin.minicrm.service;

import org.springframework.data.domain.Page;
import ru.savin.minicrm.entity.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(Long theId); // todo методам find лучше возвращать Optional

    Employee save(Employee theEmployee);

    void delete(Long theId);

    List<Employee> searchBy(String theName);

    Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

}
