package com.example.helpdesk.controllers;

import com.example.helpdesk.entities.Ticket;
import com.example.helpdesk.entities.User;
import com.example.helpdesk.repositories.UserRepository;
import com.example.helpdesk.services.TicketService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/app/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public TicketController(TicketService ticketService,
                            UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listTickets(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            model.addAttribute("tickets", ticketService.findAll());
        } else {
            model.addAttribute("tickets", ticketService.findByUser(user));
        }

        return "ticket/index";

    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticket/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result,
                         @AuthenticationPrincipal UserDetails userDetails){
        if (result.hasErrors()) {
            return "ticket/create";
        }

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow();

        ticket.setUser(user);

        ticketService.save(ticket);
        return "redirect:/app/tickets";


    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           @Valid @ModelAttribute("ticket") Ticket ticket,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "ticket/edit";
        }

        ticket.setId(id);
        ticketService.save(ticket);
        return "redirect:/app/tickets";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        ticketService.deleteById(id);
        return "redirect:/app/tickets";
    }




}
