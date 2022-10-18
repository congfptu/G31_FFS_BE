package com.example.g31_ffs_fe.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "freelancer")
public class Freelancer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_career_id")
    private Subcareer subCareer;

    @OneToMany(mappedBy = "freelancer")
    private Set<Education> educations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "freelancer")
    private Set<Workexperience> workexperiences = new LinkedHashSet<>();

    public Set<Workexperience> getWorkexperiences() {
        return workexperiences;
    }

    public void setWorkexperiences(Set<Workexperience> workexperiences) {
        this.workexperiences = workexperiences;
    }

    public Set<Education> getEducations() {
        return educations;
    }

    public void setEducations(Set<Education> educations) {
        this.educations = educations;
    }

    public Subcareer getSubCareer() {
        return subCareer;
    }

    public void setSubCareer(Subcareer subCareer) {
        this.subCareer = subCareer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Double getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(Double costPerHour) {
        this.costPerHour = costPerHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

}