package com.example.helpdesk.controllers;

import com.example.helpdesk.dto.LoginRequest;
import com.example.helpdesk.dto.RegisterRequest;
import com.example.helpdesk.entities.Role;
import com.example.helpdesk.entities.User;
import com.example.helpdesk.repositories.RoleRepository;
import com.example.helpdesk.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.catalina.Authenticator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginForm(Authentication authentication,Model model,
                            @RequestParam(value = "logout", required = false) String logout) {

       if (authentication != null && authentication.isAuthenticated()) {
           return "redirect:/app/ticket/index";
       }
        model.addAttribute("loginRequest", new LoginRequest());

        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }

        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest dto,
                        BindingResult result) {

        if(result.hasErrors()) {
            return "auth/login";
        }

        try {
            var authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(authToken);
            return "redirect:/app/dashboard";

        } catch (AuthenticationException e) {
            result.reject("loginError", "Invalid username or password");
            return "auth/login";
        }
    }


    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterRequest req,
                           BindingResult result) {

        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            result.rejectValue("username", "", "Username already exists");
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        Role userRole = roleRepository
                .findByName("ROLE_USER")
                .orElseThrow();

        User user = new User();
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(userRole);

        userRepository.save(user);

        return "redirect:/login?registered";
    }


    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return "redirect:/login?logout";
        }



}

