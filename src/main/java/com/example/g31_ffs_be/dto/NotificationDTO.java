package com.example.g31_ffs_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class NotificationDTO {
    private int postId;
    private String postTitle;
    private String userId;
    private String userName;
    private String avatar;
    private int type;
    private String time;



}
