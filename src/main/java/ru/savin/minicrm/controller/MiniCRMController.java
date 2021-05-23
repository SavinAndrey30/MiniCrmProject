package ru.savin.minicrm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.savin.minicrm.entity.Employee;
import ru.savin.minicrm.exception.EmployeeNotFoundException;
import ru.savin.minicrm.service.EmployeeService;
import ru.savin.minicrm.util.FileUploadUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/crm")
public class MiniCRMController {

    private final EmployeeService employeeService;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public MiniCRMController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("")
    public String findAll(Model model) {
        return findPaginated(1, "firstName", "asc", model);
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(@ModelAttribute("employee") Employee employee) {
        return "/employee/add-employee-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("employee") @Valid Employee employee,
                       BindingResult bindingResult,
                       @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();

        if (multipartFile.getSize() == 0 || originalFilename == null) {
            bindingResult.rejectValue("photo", "error.employee", "Please, load a photo with the correct size and " +
                    "filename");
        }

        if (bindingResult.hasErrors()) {
            return "/employee/add-employee-form";
        }

        //StringUtils from springframework package
        String fileName = StringUtils.cleanPath(originalFilename);
        employee.setPhoto(fileName);

        employeeService.save(employee);

        String uploadDir = uploadPath + "/" + employee.getId();
        //Custom class FileUploadUtil
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/crm";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(Model model, @RequestParam("id") Long id) {
        Employee employee =
                employeeService.findById(id)
                        .orElseThrow(() -> {
                            logger.info("/showFormForUpdate: Employee with the id " + id + " is not " +
                                    "found");
                            return new EmployeeNotFoundException("Employee with the id " + id + " is not " +
                                    "found");
                        });

        model.addAttribute("employee", employee);
        return "/employee/update-employee-form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("employee") @Valid Employee employee,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/employee/update-employee-form";
        }

        Long id = employee.getId();
        employeeService.findById(id)
                .map(dbEmployee -> {
                    employee.setPhoto(dbEmployee.getPhoto());
                    return employee;
                })
                .map(employeeService::save)
                .map(value -> "redirect:/crm")
                .orElseThrow(() -> {
                    logger.info("/update: Employee with the id " + id + " is not " +
                            "found");
                    return new EmployeeNotFoundException("Employee with the id " + id + " is not " +
                            "found");
                });

//        Optional<Employee> dbEmployee = employeeService.findById(employee.getId());
//        dbEmployee.ifPresent(value -> employee.setPhoto(value.getPhoto()));

        employeeService.save(employee);

        return "redirect:/crm";
    }

    @GetMapping("/showEmployeeDetails")
    public String showEmployeeDetails(Model model, @RequestParam("id") Long id) {
        Employee employee =
                employeeService.findById(id)
                        .orElseThrow(() -> {
                            logger.info("/showEmployeeDetails: Employee with the id " + id + " is not " +
                                    "found");
                            return new EmployeeNotFoundException("Employee with the id " + id + " is not " +
                                    "found");
                        });
        model.addAttribute("employee", employee);
        return "/employee/employee-details";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        employeeService.delete(id);

        String uploadDir = uploadPath + "/" + id;
        Path uploadPath = Path.of(uploadDir);

        //removing the folder with photo for deleted entity
        try {
            FileSystemUtils.deleteRecursively(uploadPath);
        } catch (IOException ignored) {
            logger.warning("couldn't remove the folder with photo for deleted employee with the id " + id);
        }

        return "redirect:/crm";
    }

    @GetMapping("/search")
    public String search(@RequestParam("employeeName") String employeeName, Model model) {
        List<Employee> employees = employeeService.searchBy(employeeName);

        model.addAttribute("employees", employees);

        return "/employee/list-employees";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDirection") String sortDirection, Model model) {
        int pageSize = 5;
        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDirection);

        List<Employee> employees = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("sortField", sortField);
        model.addAttribute("reverseSortDirection", sortDirection.equals(Sort.Direction.ASC.name()) ?
                Sort.Direction.DESC.name() : Sort.Direction.ASC.name());

        model.addAttribute("employees", employees);

        return "/employee/list-employees";
    }


}
