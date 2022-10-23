package com.example.g31_ffs_be.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "workexperience")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id")
    @JsonIgnore
    private Freelancer freelancer;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "position")
    private String position;

    @Column(name = "description")
    private String description;

    @Column(name = "month_from")
    private Integer monthFrom;

    @Column(name = "year_from")
    private Integer yearFrom;

    @Column(name = "month_to")
    private Integer monthTo;

    @Column(name = "year_to")
    private Integer yearTo;

}