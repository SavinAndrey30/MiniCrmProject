package ru.savin.minicrm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.savin.minicrm.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

//    List<Employee> findAllByOrderByLastName(); // todo remove or uncomment

    // search by name
    List<Employee> findByFirstNameContainsOrLastNameContainsAllIgnoreCase(String name, String lName);
}
