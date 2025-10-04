package com.nt.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nt.CakeService.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("error", null);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            Model model) {
        String error = userService.registerUser(username, password, email);
        if (error != null) {
            model.addAttribute("error", error);
            return "register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("error", null);
        return "login";
    }
}