package com.example.g31_ffs_be.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @Size(max = 45)
    @Column(name = "id", nullable = false, length = 45)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private Recruiter createBy;

    @Size(max = 255)
    @Column(name = "job_title")
    private String jobTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_career_id")
    private Subcareer subCareer;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 255)
    @Column(name = "attach")
    private String attach;

    @Size(max = 255)
    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "time")
    private Instant time;

    @Size(max = 255)
    @Column(name = "area")
    private String area;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Staff approvedBy;

    @OneToMany(mappedBy = "job")
    private Set<JobRequest> jobRequests = new LinkedHashSet<>();

    @OneToMany(mappedBy = "job")
    private Set<JobSaved> jobSaveds = new LinkedHashSet<>();

    @OneToMany(mappedBy = "job")
    private Set<Notification> notifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "job")
    private Set<JobSkill> jobSkills = new LinkedHashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Recruiter getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Recruiter createBy) {
        this.createBy = createBy;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Subcareer getSubCareer() {
        return subCareer;
    }

    public void setSubCareer(Subcareer subCareer) {
        this.subCareer = subCareer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Staff getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Staff approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Set<JobRequest> getJobRequests() {
        return jobRequests;
    }

    public void setJobRequests(Set<JobRequest> jobRequests) {
        this.jobRequests = jobRequests;
    }

    public Set<JobSaved> getJobSaveds() {
        return jobSaveds;
    }

    public void setJobSaveds(Set<JobSaved> jobSaveds) {
        this.jobSaveds = jobSaveds;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<JobSkill> getJobSkills() {
        return jobSkills;
    }

    public void setJobSkills(Set<JobSkill> jobSkills) {
        this.jobSkills = jobSkills;
    }

}