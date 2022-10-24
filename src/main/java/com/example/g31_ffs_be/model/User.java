package com.example.g31_ffs_be.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User {
    @Id
    @Column(name = "user_id", nullable = false, length = 45)
    private String id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Account account;

    @Column(name = "account_balance")
    private Double accountBalance;

    @Column(name = "phone", length = 45)
    private String phone;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "address")
    private String address;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @OneToOne(mappedBy = "user")
    private Recruiter recruiter;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Freelancer freelancer;

    @OneToMany(mappedBy = "from")
    private Set<Report> reports = new LinkedHashSet<>();

    @Column(name = "is_banned")
    private Boolean isBanned;


    @OneToMany(mappedBy = "to")
    private Set<Feedback> feedbackTos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "from")
    private Set<Feedback> feedbackFroms = new LinkedHashSet<>();

    @Size(max = 255)
    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "reset_password_time")
    private Instant resetPasswordTime;

}