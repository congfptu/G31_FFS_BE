package com.example.g31_ffs_be.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@DynamicUpdate
@DynamicInsert
public class Account {
    @Id
    @Column(name = "id", nullable = false, length = 45)
    private String id;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL,fetch = FetchType.LAZY, optional = false)
    private User user;
    @OneToOne(mappedBy = "account",cascade = CascadeType.ALL,fetch = FetchType.LAZY, optional = false)
    private Staff staff;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Role role;

    @Column(name = "password")
    private String password;


}