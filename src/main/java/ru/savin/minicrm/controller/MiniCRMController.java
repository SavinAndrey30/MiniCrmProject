package ru.savin.minicrm.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.savin.minicrm.entity.Employee;
import ru.savin.minicrm.service.EmployeeService;
import ru.savin.minicrm.util.FileUploadUtil;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/crm")
public class MiniCRMController {

    private EmployeeService employeeService;

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

        if (multipartFile.getSize() == 0) {
            bindingResult.rejectValue("photo", "error.employee", "Please, load a photo");
        }

        if (bindingResult.hasErrors()) {
            return "/employee/add-employee-form";
        }

        //StringUtils from springframework package
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        employee.setPhoto(fileName);

        employeeService.save(employee);

        String uploadDir = uploadPath + "/" + employee.getId();
        //Custom class FileUploadUtil
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/crm";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(Model model, @RequestParam("id") Long id) {
        model.addAttribute("employee", employeeService.findById(id));
        return "/employee/update-employee-form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("employee") @Valid Employee employee,
                         BindingResult bindingResult) {
        Employee dbEmployee = employeeService.findById(employee.getId());
        employee.setPhoto(dbEmployee.getPhoto());

        if (bindingResult.hasErrors()) {
            return "/employee/update-employee-form";
        }

        employeeService.save(employee);

        return "redirect:/crm";
    }

    @GetMapping("/showEmployeeDetails")
    public String showEmployeeDetails(Model model, @RequestParam("id") Long id) {
        model.addAttribute("employee", employeeService.findById(id));
        return "/employee/employee-details";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        employeeService.delete(id);

        String uploadDir = uploadPath + "/" + id;
        System.out.println(uploadDir);
        Path uploadPath = Paths.get(uploadDir);

        //removing the folder with photo for deleted entity
        try {
            FileSystemUtils.deleteRecursively(uploadPath);
        } catch (IOException ignored) {

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
                                @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 5;
        Page<Employee> page = employeeService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Employee> employees = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("employees", employees);

        return "/employee/list-employees";
    }


}
