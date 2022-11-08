package com.example.g31_ffs_be.model;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;

@Entity
@Table(name = "staff")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
public class Staff {
    @Id
    @Column(name = "id", nullable = false, length = 45)
    private String id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id", nullable = false)
    @JsonIgnore
    private Account account;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;
    @Column(name = "fullname")
    private String fullName;

    @Column(name = "isActive")
    private Boolean isActive;


}