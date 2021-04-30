package ru.savin.minicrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.savin.minicrm.entity.User;
import ru.savin.minicrm.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class AdminController {

    private UserService userService; // todo non final field

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showCrmUsers(Model model) {
        List<User> users = userService.findAll(); // todo inline

        model.addAttribute("users", users);

        return "/user/list-users";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);

        return "redirect:/users";
    }


}
