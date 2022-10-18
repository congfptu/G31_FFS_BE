package com.example.g31_ffs_be.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "freelancer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Freelancer {
    @Id
    @Column(name = "freelancer_id", nullable = false, length = 45)
    private String id;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "cost_per_hour")
    private Double costPerHour;

    @Column(name = "description")
    private String description;

    @Column(name = "cv")
    private String cv;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_career_id")
    private Subcareer subCareer;

    @OneToMany(mappedBy = "freelancer")
    private Set<Education> educations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "freelancer")
    private Set<Workexperience> workexperiences = new LinkedHashSet<>();

}