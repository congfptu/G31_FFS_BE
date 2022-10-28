package com.example.g31_ffs_be.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "recruiter")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Recruiter {
    @Id
    @Column(name = "recruiter_id", nullable = false, length = 45)
    private String id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User user;

    @Column(name = "company_intro")
    private String companyIntro;

    @Column(name = "website")
    private String website;

    @Column(name = "tax_number")
    private String taxNumber;


    @ManyToOne()
    @JoinColumn(name = "career_id")
    private Career career;

    @Size(max = 255)
    @Column(name = "company_name")
    private String companyName;

}