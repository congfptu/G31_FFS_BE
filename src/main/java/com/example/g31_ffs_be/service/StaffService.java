package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.StaffAdminDto;
import com.example.g31_ffs_be.model.Freelancer;
import com.example.g31_ffs_be.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface StaffService {
void addStaff(Staff s);
List<StaffAdminDto> getAllStaffs();
    List<StaffAdminDto> getStaffByName(String name,int pageNo,int pageSize);
    void banStaff(String id);

}
