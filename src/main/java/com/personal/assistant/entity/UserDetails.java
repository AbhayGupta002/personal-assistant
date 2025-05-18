package com.personal.assistant.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "contact_number", nullable = false, unique = true)
    private long contactNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private UserLogin userLogin;

    @Column(name = "address", nullable = false, unique = true)
    private String address;

    @Column(name = "status", nullable = false, unique = true)
    private String status;
}