package com.example.login.domain;

import lombok.Data;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.DigestUtils;

import javax.persistence.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column
    private String mail;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String category;
    @Column
    private String major;
    @Column
    private boolean push_switch = true;
}
