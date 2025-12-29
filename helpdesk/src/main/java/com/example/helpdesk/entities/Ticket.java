package com.example.helpdesk.entities;

import com.example.helpdesk.validations.ValidTicketStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5)
    private String title;

    @NotBlank
    private String category;

    private LocalDate createdDate;

    @ValidTicketStatus
    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
