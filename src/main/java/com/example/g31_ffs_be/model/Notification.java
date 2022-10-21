package com.example.g31_ffs_be.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @Size(max = 45)
    @Column(name = "id", nullable = false, length = 45)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Size(max = 255)
    @Column(name = "link")
    private String link;

    @Column(name = "date")
    private Instant date;

    @Column(name = "status")
    private Boolean status;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}