package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class DashboardDTO {
    List<String> label;
    List<Integer> freelancers;
    List<Integer> recruiters;
    List<Integer> posts;
    List<Integer> applies;
    List<Double> revenues;
    Integer totalCareer;
    Integer totalSubCareer;
    Integer totalMemberShip;
    Integer totalReport;
}
