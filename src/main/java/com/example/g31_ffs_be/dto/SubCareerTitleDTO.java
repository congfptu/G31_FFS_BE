package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Subcareer;
import lombok.Data;
import java.util.Set;

@Data
public class SubCareerTitleDTO {
    String title;
    Set<Subcareer> data;
}
