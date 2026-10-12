package com.ficklebytes.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "allowed_origins")
public class AllowedOrigin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
}
