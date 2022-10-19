package com.example.g31_ffs_be.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "freelancer_skill")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(name = "freelancer_id")
    private String freelancer;

    @Column(name = "skill_id")
    private Integer skill;

}