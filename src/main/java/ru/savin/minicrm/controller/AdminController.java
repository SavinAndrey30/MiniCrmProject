package ru.savin.minicrm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.savin.minicrm.service.UserService;

@Controller
@RequestMapping("/users")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showCrmUsers(Model model) {

        model.addAttribute("users", userService.findAll());

        return "/user/list-users";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);

        return "redirect:/users";
    }


}
