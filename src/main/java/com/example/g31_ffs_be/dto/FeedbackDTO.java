package com.example.g31_ffs_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class FeedbackDTO {
Integer id;
Integer star;
String content;
@JsonFormat(pattern="dd-MM-yyyy HH:mm")
LocalDateTime date;
String fromUserId;
int jobId;
String toUserId;
String fromFullName;
String fromAvatar;
}
