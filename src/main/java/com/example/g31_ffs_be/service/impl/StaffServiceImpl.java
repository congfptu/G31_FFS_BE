package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.RecruiterAdminDto;
import com.example.g31_ffs_be.dto.StaffAdminDto;
import com.example.g31_ffs_be.dto.StaffDto;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.model.Recruiter;
import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.repository.AccountRepository;
import com.example.g31_ffs_be.repository.StaffRepository;
import com.example.g31_ffs_be.service.StaffService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired StaffRepository staffRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public void addStaff(Staff s) {
        staffRepository.save(s);
    }

    @Override
    public List<StaffAdminDto> getAllStaffs() {

        List<StaffAdminDto> lists=new ArrayList<>();
        try{
        for (Staff a:staffRepository.findAll()) {
            StaffAdminDto sad=new StaffAdminDto();
            sad.setId(a.getId());
            sad.setPhone(a.getPhone());
            sad.setFullName(a.getFullName());
            sad.setAddress(a.getAddress());
            sad.setEmail(a.getAccount().getEmail());
            lists.add(sad);
        }}
        catch (Exception e){

        }
        return lists;
    }
    private StaffAdminDto mapToStaffAdminDTO(Staff staff){
        StaffAdminDto staffAdminDto=mapper.map(staff,StaffAdminDto.class);
        return staffAdminDto;
    }
    private Staff mapDtoToStaff(StaffDto staffDto){
        Staff staff=mapper.map(staffDto,Staff.class);
        return staff;
    }
    @Override
    public APIResponse<StaffAdminDto> getStaffByName(String name, int pageNo, int pageSize) {
        APIResponse apiResponse=new APIResponse<>();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Staff> page = staffRepository.getStaffByName(name,pageable);
        List<Staff> staffs=page.getContent();
        List<StaffAdminDto> sads=new ArrayList<>();
        for (Staff s: staffs){
            StaffAdminDto sad=new StaffAdminDto();
            sad=mapToStaffAdminDTO(s);
            sad.setEmail(s.getAccount().getEmail());
            sads.add(sad);
        }
        apiResponse.setResults(sads);
        apiResponse.setTotalPages(page.getTotalPages());
        apiResponse.setTotalResults(page.getTotalElements());
        apiResponse.setPageIndex(pageNo+1);
        return apiResponse;
    }
    @Override
    public void updateStaff(StaffDto staffDto) {
    Account account=accountRepository.getReferenceById(staffDto.getId());
    account.setStaff(mapDtoToStaff(staffDto));
    account.setEmail(staffDto.getEmail());
        accountRepository.save(account);
    }


    @Override
    public void banStaff(Staff s) {
        s.setIsActive(s.getIsActive()?false:true);
        staffRepository.save(s);
    }
}
