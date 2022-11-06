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
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jobs")
@DynamicUpdate
@DynamicInsert
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
     Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "create_by")
    private Recruiter createBy;

    @Column(name = "job_title")
    private String jobTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_career_id")
    private Subcareer subCareer;

    @Size(max = 2000)
    @Column(name = "description",length = 2000)
    private String description;

    @Size(max = 255)
    @Column(name = "attach")
    private String attach;

    @Column(name = "payment_type")
    private int paymentType;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "time")
    private LocalDateTime time;

    @Size(max = 255)
    @Column(name = "area")
    private String area;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_approved")
    private Integer isApproved;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by",updatable = false,insertable = false)
    private Staff approvedBy;

    @ManyToMany
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills;

    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "job_saved",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "freelancer_id"))
    private Set<Freelancer> freelancers;

    @Column(name = "fee")
    private Double fee;

    @Column(name = "top_time",updatable = false,insertable = false)
    private LocalDateTime topTime;


    @OneToMany(mappedBy = "job")
    @Fetch(FetchMode.SUBSELECT)
    private Set<JobRequest> jobRequests = new LinkedHashSet<>();

}