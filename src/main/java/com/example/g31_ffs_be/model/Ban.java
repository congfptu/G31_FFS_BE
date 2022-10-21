package com.example.g31_ffs_be.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "ban")
@Getter
@Setter
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "date")
    private Instant date;

    @Column(name = "user_id")
    private String user;

    @Column(name = "banned_by")
    private String bannedBy;


    @Column(name = "type_ban")
    private Integer typeBan;

}