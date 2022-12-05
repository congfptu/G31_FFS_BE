package com.example.g31_ffs_be.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerAdminDto {
    String id;
    String email;
    String fullName;
    double accountBalance;
    Boolean isBanned;
    String currentService;
    String avatar;
    double totalMoneyUsed;
}
