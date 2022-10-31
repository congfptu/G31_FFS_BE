package com.example.g31_ffs_be.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "account_role")
public class AccountRole {
    @Id
    @Size(max = 45)
    @Column(name = "account_id", nullable = false, length = 45)
    private String id;

    @Column(name = "role_id")
    private Integer role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

}