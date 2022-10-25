package com.example.g31_ffs_be.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "job_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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


}