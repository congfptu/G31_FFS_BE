package com.example.g31_ffs_be.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "fee_history")
public class FeeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "date")
    private Instant date;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by")
    private Staff updateBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Staff getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Staff updateBy) {
        this.updateBy = updateBy;
    }

}