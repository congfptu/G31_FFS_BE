package com.example.g31_ffs_be.dto;

import lombok.Data;


@Data
public class JWTAuthResponse {
    private String userId;
    private String role;
    private Boolean isMemberShip;
    private double accountBalance;
    private String accessToken;
    private String tokenType = "Bearer";


}