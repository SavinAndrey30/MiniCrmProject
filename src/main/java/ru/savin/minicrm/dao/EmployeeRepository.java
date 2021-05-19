package ru.savin.minicrm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.savin.minicrm.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //todo удалить, если альтернатива подходит
    // search by name
    List<Employee> findByFirstNameContainsOrLastNameContainsAllIgnoreCase(String name, String lastName);

    @Query("select e from Employee e where lower(e.firstName) like lower(concat('%', :keyword, '%')) " +
            "or lower(e.lastName) like lower(concat('%', :keyword, '%'))")
    List<Employee> findByFirstNameOrLastName(@Param("keyword") String keyword);
}
