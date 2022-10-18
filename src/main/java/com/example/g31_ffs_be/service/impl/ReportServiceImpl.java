package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.model.Report;
import com.example.g31_ffs_be.repository.ReportRepository;
import com.example.g31_ffs_be.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired ReportRepository repo;
    @Override
    public void addReport(Report r) {
        try{
            repo.save(r);
        }
        catch (Exception e) {

        }
    }

    @Override
    public Page<Report> getReportByPaging(int offset, int pagesize) {
       try{
           return  repo.findAll(PageRequest.of(offset, pagesize));
       }
       catch (Exception e){
       }
       return null;
    }
}
