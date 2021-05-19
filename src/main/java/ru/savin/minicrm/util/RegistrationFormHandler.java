package ru.savin.minicrm.util;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.savin.minicrm.dto.FormUser;
import ru.savin.minicrm.entity.User;
import ru.savin.minicrm.service.UserService;

import java.util.Optional;


public class RegistrationFormHandler {

    public static boolean userExists(BindingResult bindingResult, UserService userService, FormUser formUser,
                                     Model model) {
        return bindingResultErrorsExist(bindingResult) || usernameExists(userService, formUser, model) || emailExists(userService, formUser, model);
    }

    public static boolean bindingResultErrorsExist(BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    public static boolean usernameExists(UserService userService, FormUser formUser, Model model) {
        Optional<User> existingByUsername = userService.findByUserName(formUser.getUserName());
        if (existingByUsername.isPresent()) {
            model.addAttribute("formUser", new FormUser());
            model.addAttribute("registrationError", "User name already exists.");

            return true;
        }
        return false;
    }

    public static boolean emailExists(UserService userService, FormUser formUser, Model model) {
        Optional<User> existingByEmail = userService.findByEmail(formUser.getEmail());
        if (existingByEmail.isPresent()) {
            model.addAttribute("registrationErrorEmail", "User with the email " + formUser.getEmail() + " already " +
                    "exists.");

            return true;
        }
        return false;
    }

}
