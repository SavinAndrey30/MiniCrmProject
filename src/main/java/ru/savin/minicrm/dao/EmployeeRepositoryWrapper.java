package ru.savin.minicrm.dao;

import org.springframework.stereotype.Component;
import ru.savin.minicrm.entity.Employee;

import java.util.List;
import java.util.Optional;

//TODO удалить wrapper после проверки
@Component
public class EmployeeRepositoryWrapper {

    private final EmployeeRepository employeeRepository;

    public EmployeeRepositoryWrapper(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findByFirstNameOrLastName(String keyword) {
        return employeeRepository.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(keyword, keyword);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
