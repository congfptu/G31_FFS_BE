package com.example.g31_ffs_fe.dto;

import com.example.g31_ffs_fe.model.Role;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data

public class AccountDto {
    String id;
    String email;
    @ElementCollection(fetch = FetchType.LAZY)
    Set<Role> roles=new LinkedHashSet<>();
}
