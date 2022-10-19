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
    String accountBalance;
    Boolean isBanned;
}
