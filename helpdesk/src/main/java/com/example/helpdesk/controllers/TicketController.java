package com.example.helpdesk.controllers;

import com.example.helpdesk.entities.Ticket;
import com.example.helpdesk.entities.User;
import com.example.helpdesk.repositories.TicketRepository;
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
@RequestMapping("/app/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public TicketController(TicketService ticketService,
                            UserRepository userRepository,
                            TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @GetMapping("/index")
    public String listTickets(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            model.addAttribute("tickets", ticketService.findAll());
        } else {
            model.addAttribute("tickets", ticketService.findByUser(user));
        }

        return "app/ticket/index";

    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "app/ticket/create";
    }


    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("ticket") Ticket ticket,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails userDetails) {

        if (ticket.getStatus() == null) {
            ticket.setStatus("OPEN");
        }

        if (result.hasErrors()) {
            return "app/ticket/create";
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();
        ticket.setUser(user);
        ticketService.save(ticket);

        return "redirect:/app/ticket/index";
    }


    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {

            Ticket ticket = ticketService.findById(id);

            model.addAttribute("ticket", ticket);
            return "app/ticket/edit";

        }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("ticket") Ticket ticket,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails userDetails) {

        if (result.hasErrors()) {
            return "app/ticket/edit";
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();
        ticket.setUser(user);
        ticket.setId(id);
        ticketService.save(ticket);

        return "redirect:/app/ticket/index";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        ticketService.deleteById(id);
        return "redirect:/app/ticket/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String title,
                         @RequestParam String status,
                         Model model) {

        model.addAttribute("tickets",
                ticketRepository.findByTitleContainingIgnoreCaseAndStatus(title, status));

        return "ticket/index";
    }




}
