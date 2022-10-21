package com.example.g31_ffs_be.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "job_request")
public class JobRequest {
    @Id
    @Size(max = 45)
    @Column(name = "id", nullable = false, length = 45)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "status")
    private Integer status;

    @Column(name = "apply_date")
    private Instant applyDate;

    @Column(name = "approved_date")
    private Instant approvedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Instant getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Instant applyDate) {
        this.applyDate = applyDate;
    }

    public Instant getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Instant approvedDate) {
        this.approvedDate = approvedDate;
    }

}