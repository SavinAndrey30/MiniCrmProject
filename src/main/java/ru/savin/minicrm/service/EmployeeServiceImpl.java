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
    public Employee findById(Long theId) {
        // todo много лишнего, используй метод из Optional.orElseThrow
        return employeeRepository
                .findById(theId)
                .orElseThrow(
                () -> new EmployeeNotFoundException("Employee with the id " + theId + " is not found")
        );
    }

    @Override
    public Employee save(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }

    @Override
    public void delete(Long theId) { // todo theId плохое имя
        // todo много лишнего, используй метод из Optional.orElseThrow
        employeeRepository
                .findById(theId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with the id " + theId + " is not found"));
        employeeRepository.deleteById(theId);
    }

    @Override
    public List<Employee> searchBy(String theName) {

        List<Employee> results = null;

        if (theName != null && theName.trim().length() > 0) {
            results = employeeRepository.findByFirstNameContainsOrLastNameContainsAllIgnoreCase(theName, theName); // todo передача аргументов таким способом - плохое решение, если ты хочешь сделать такое, напиши внутри репозитория метод с одним аргументом, внутри него сделай такой вот вызов, сейчас создется ощущение что это просто копипаста и ошибка при чтении кода
        } else {
            results = findAll();
        }

        // todo можно сразу без лишних переменных result написать  if else, будет короче и намного лучше

        return results;
    }

    // todo sortDirection ну такое
    @Override
    public Page<Employee> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        // todo -1 потому что нумерация с 0 начинается?
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return employeeRepository.findAll(pageable);
    }
}
