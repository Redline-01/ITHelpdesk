package com.example.helpdesk.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

}
