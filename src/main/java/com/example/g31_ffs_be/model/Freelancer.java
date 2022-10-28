package com.example.g31_ffs_be.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "freelancer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
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
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "freelancer")
    private List<WorkExperience> workExperiences = new ArrayList<>();
    @OneToMany
    @JoinTable(name = "freelancer_skill",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;


}