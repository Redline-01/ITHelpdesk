package com.example.helpdesk.repositories;

import com.example.helpdesk.entities.Ticket;
import com.example.helpdesk.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUser(User user);

    List<Ticket> findByStatus(String status);

    List<Ticket> findByCategory(String category);

    @Query("SELECT t.status, COUNT(t) FROM Ticket t GROUP BY t.status")
    List<Object[]> countTicketsByStatus();

}
