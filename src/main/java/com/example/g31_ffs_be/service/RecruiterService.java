package com.example.g31_ffs_be.service;




import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.RecruiterAdminDto;
import com.example.g31_ffs_be.dto.RecruiterDetailDTO;
import com.example.g31_ffs_be.dto.RegisterDto;

import java.util.List;

public interface RecruiterService {
    APIResponse<RecruiterAdminDto> getRecruiterByName(String name, int pageNo, int pageSize);
    RecruiterDetailDTO getDetailRecruiter(String id);
    List<RecruiterAdminDto> getTop5RecruiterByName(String name);

    Boolean updateProfile(RegisterDto registerDto);
    Boolean updateProfileRecruiter(RegisterDto registerDto);

    RecruiterDetailDTO getProfileRecruiterByFreelancer(String id,String freelancerId);

}
