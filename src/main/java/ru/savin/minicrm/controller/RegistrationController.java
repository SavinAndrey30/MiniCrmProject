package ru.savin.minicrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.savin.minicrm.dto.FormUser;
import ru.savin.minicrm.entity.User;
import ru.savin.minicrm.service.UserService;

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
        // todo code style
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor); // todo inline
    }

    @GetMapping("/showRegistrationForm")
    public String showMyLoginPage(Model theModel) {

        theModel.addAttribute("formUser", new FormUser());

        return "/user/registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("formUser") FormUser formUser,
            BindingResult theBindingResult,
            Model theModel) {

        String userName = formUser.getUserName();
        logger.info("Processing registration form for: " + userName);

        if (theBindingResult.hasErrors()) {
            return "/user/registration-form";
        }

        User existingByUsername = userService.findByUserName(userName); // todo change findByUserName to return Optional
        if (existingByUsername != null) {
            theModel.addAttribute("formUser", new FormUser());
            theModel.addAttribute("registrationError", "User name already exists.");

            logger.warning("User with the name " + formUser.getUserName() + " already exists."); // todo warning for why?
            return "/user/registration-form";
        }

        User existingByEmail = userService.findByEmail(formUser.getEmail()); // todo change findByEmail to return Optional
        if (existingByEmail != null) {
            theModel.addAttribute("registrationErrorEmail", "User with the email " + formUser.getEmail() + " already exists.");

            logger.warning("User with the email " + formUser.getEmail() + " already exists."); // todo warning for why?
            return "/user/registration-form";
        }
        // todo все проверки лучше вынести в другой класс и каждую проверку в отдельный метод.
        // затем сделать метод который проводит все проверки и возвращает результат, а в контроллере этот результат
        // конвертировать в нужную страничку


        userService.save(formUser);

        logger.info("Successfully created user: " + userName);

        return "/user/registration-confirmation";
    }
}
