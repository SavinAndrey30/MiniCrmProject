package ru.savin.minicrm.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.savin.minicrm.dao.EmployeeRepository;
import ru.savin.minicrm.entity.Employee;
import ru.savin.minicrm.exception.EmployeeNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void delete(Long id) {
        employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with the id " + id + " is not found"));

        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> searchBy(String keyword) {
        if (keyword != null && keyword.trim().length() > 0) {
            return EmployeeServiceImpl.findByFirstNameOrLastNameUtil(employeeRepository, keyword);
        } else {
            return findAll();
        }
    }

    @Override
    public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        int pageNumberZeroBased = pageNo - 1;
        Pageable pageable = PageRequest.of(pageNumberZeroBased, pageSize, sort);
        return employeeRepository.findAll(pageable);
    }

    public static List<Employee> findByFirstNameOrLastNameUtil(EmployeeRepository employeeRepository, String keyword) {
        return employeeRepository.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(keyword, keyword);
    }
}
