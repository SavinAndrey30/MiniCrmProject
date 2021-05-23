package ru.savin.minicrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.savin.minicrm.dto.FormUser;
import ru.savin.minicrm.service.UserService;
import ru.savin.minicrm.util.RegistrationFormValidator;

import javax.validation.Valid;
import java.util.logging.Logger;


@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/showRegistrationForm")
    public String showMyLoginPage(Model theModel) {

        theModel.addAttribute("formUser", new FormUser());

        return "/user/registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("formUser") FormUser formUser,
            BindingResult bindingResult,
            Model model) {

        String userName = formUser.getUserName();
        logger.info("Processing registration form for: " + userName);

        if (RegistrationFormValidator.userExists(bindingResult, userService, formUser, model)) {
            return "/user/registration-form";
        }

        userService.save(formUser);

        logger.info("Successfully created user: " + userName);

        return "/user/registration-confirmation";
    }
}
