package com.example.helpdesk.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class AppController {
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Merr username të loguar
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "app/dashboard"; // Thymeleaf template
    }
}
