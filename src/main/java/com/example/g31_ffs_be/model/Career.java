package com.example.g31_ffs_be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "career")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Career {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @Column(name = "Name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "career")
    private Set<Subcareer> subcareers = new LinkedHashSet<>();

}