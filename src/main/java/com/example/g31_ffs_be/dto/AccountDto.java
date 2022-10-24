package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Role;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data

public class AccountDto {
    String id;
    String email;
    String password;
}
