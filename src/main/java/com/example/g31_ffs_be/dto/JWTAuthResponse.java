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
    private String avatar;
    private double feeApplyJob;
    private double feePostJob;
    private double feeViewProfile;
    private int unReadNotification;
    private String email;
    private int currentServiceId;
    private String currentServiceName;


}
