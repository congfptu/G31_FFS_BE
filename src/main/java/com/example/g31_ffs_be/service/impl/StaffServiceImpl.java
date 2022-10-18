package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.StaffAdminDto;
import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.repository.StaffRepository;
import com.example.g31_ffs_be.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired StaffRepository repo;
    @Override
    public void addStaff(Staff s) {
        repo.save(s);
    }

    @Override
    public List<StaffAdminDto> getAllStaffs() {

        List<StaffAdminDto> lists=new ArrayList<>();
        try{
        for (Staff a:repo.findAll()) {
            StaffAdminDto sad=new StaffAdminDto();
            sad.setId(a.getId());
            sad.setPhone(a.getPhone());
            sad.setFullname(a.getFullname());
            sad.setAddress(a.getAddress());
            sad.setEmail(a.getAccount().getEmail());
            lists.add(sad);
        }}
        catch (Exception e){

        }
        return lists;
    }

    @Override
    public List<StaffAdminDto> getStaffWithPaging(int offset, int pageSize) {
        List<StaffAdminDto> lists=new ArrayList<>();
        try{
            Page<Staff> staffs=repo.findAll(PageRequest.of(offset, pageSize));
        if(staffs!=null &&staffs.hasContent()) {
            for (Staff a : staffs.getContent()) {
                StaffAdminDto sad = new StaffAdminDto();
                sad.setId(a.getId());
                sad.setPhone(a.getPhone());
                sad.setFullname(a.getFullname());
                sad.setAddress(a.getAddress());
                sad.setEmail(a.getAccount().getEmail());
                lists.add(sad);
            }
        }}
        catch (Exception e){
        }
        return  lists;
    }

    @Override
    public void banStaff(String id) {
        Staff s=repo.findById(id).get();
        s.setIsActive(false);
        repo.save(s);
    }
}
