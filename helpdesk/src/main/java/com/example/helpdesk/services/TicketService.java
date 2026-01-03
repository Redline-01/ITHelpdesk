package com.example.helpdesk.services;

import com.example.helpdesk.entities.Ticket;
import com.example.helpdesk.entities.User;
import com.example.helpdesk.repositories.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public List<Ticket> findByUser(User user) {
        return ticketRepository.findByUser(user);
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

}
