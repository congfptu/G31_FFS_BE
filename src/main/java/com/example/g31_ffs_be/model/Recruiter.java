package com.example.g31_ffs_be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "recruiter_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "company_intro")
    private String companyIntro;

    @Column(name = "website")
    private String website;

    @Column(name = "tax_number")
    private String taxNumber;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id")
    private Career career;

    @Size(max = 255)
    @Column(name = "company_name")
    private String companyName;

    @OneToMany(mappedBy = "createBy")
    private Set<Job> jobs = new LinkedHashSet<>();

    @Column(name = "is_active")
    private Boolean isActive;

}