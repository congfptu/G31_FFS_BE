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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "freelancer_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_career_id")
    @JsonIgnore
    private Subcareer subCareer;

    @OneToMany(mappedBy = "freelancer",fetch = FetchType.LAZY)
    private Set<Education> educations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "freelancer")
    private Set<WorkExperience> workExperiences = new LinkedHashSet<>();
    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "freelancer_skill",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills;

    @ManyToMany
    @JoinTable(name = "job_saved",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id"))
    private Set<Job> jobSaves;


    @OneToMany(mappedBy = "freelancer")
    private Set<JobRequest> jobRequests = new LinkedHashSet<>();


}