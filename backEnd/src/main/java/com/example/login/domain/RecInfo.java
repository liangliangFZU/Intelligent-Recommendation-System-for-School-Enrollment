package com.example.login.domain;

import javax.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "test")
public class RecInfo {
    @Column
    private String category;
    @Column
    private String title;
    @Column
    private String url;
    @Column
    private String date;
    @Column
    private boolean isCampus;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
}
