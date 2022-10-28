package com.example.g31_ffs_be.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
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


}