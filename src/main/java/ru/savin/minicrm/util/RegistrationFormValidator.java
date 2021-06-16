package ru.savin.minicrm.util;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.savin.minicrm.dto.FormUser;
import ru.savin.minicrm.service.UserService;

import java.util.logging.Logger;


public class RegistrationFormValidator {

    private static final Logger logger = Logger.getLogger(RegistrationFormValidator.class.getName());

    public static boolean userExists(BindingResult bindingResult, UserService userService, FormUser formUser,
                                     Model model) {
        return bindingResultErrorsExist(bindingResult) || usernameExists(userService, formUser, model) || emailExists(userService, formUser, model);
    }

    public static boolean bindingResultErrorsExist(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    public static boolean usernameExists(UserService userService, FormUser formUser, Model model) {
        String userName = formUser.getUserName();
        if (userService.existsByUserName(userName)) {
            model.addAttribute("formUser", new FormUser());
            model.addAttribute("registrationError", "User name already exists.");

            return true;
        }
        logger.info("User with the name " + userName + " already exists.");
        return false;
    }

    public static boolean emailExists(UserService userService, FormUser formUser, Model model) {
        String email = formUser.getEmail();
        if (userService.existsByEmail(email)) {
            model.addAttribute("formUser", new FormUser());
            model.addAttribute("registrationErrorEmail", "User with the email " + email + " already " +
                    "exists.");

            return true;
        }
        logger.info("User with the email " + email + " already exists.");
        return false;
    }

}
