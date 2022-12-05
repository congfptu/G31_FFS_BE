package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Benefit;
import com.example.g31_ffs_be.model.Role;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
@Data
public class ServiceDto {

    private Integer id;

    private String serviceName;

    private Double price;

    private String description;

    private Integer duration;
    private Integer timeRemain;

}
