package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.CountDto;
import com.example.g31_ffs_be.dto.DashboardDTO;
import com.example.g31_ffs_be.dto.RevenueDTO;
import com.example.g31_ffs_be.repository.UserRepository;
import com.example.g31_ffs_be.service.DashboadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DashboarServiceImpl implements DashboadService {
    @Autowired
    UserRepository userRepository;

    @Override
    public DashboardDTO getDashboardServices() {
        DashboardDTO dashboardDTO=new DashboardDTO();
        List<String> label = new ArrayList<>();
        YearMonth thisMonth  = YearMonth.now();
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        label.add(thisMonth.minusMonths(6).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(5).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(4).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(3).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(2).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(1).format(monthYearFormatter));
        label.add(thisMonth.format(monthYearFormatter));
        dashboardDTO.setLabel(label);
        List<Object[]> countFreelancers=userRepository.countFreelancer();
        List<Object[]> countRecruiters=userRepository.countRecruiter();
        List<Object[]> countPosted=userRepository.countPosted();
        List<Object[]> countApplies=userRepository.countApplies();
        List<Object[]> countRevenues=userRepository.countRevenues();

        dashboardDTO.setFreelancers(dashboadCount(countFreelancers,label));

        dashboardDTO.setRecruiters(dashboadCount(countRecruiters,label));

        dashboardDTO.setPosts(dashboadCount(countPosted,label));

        dashboardDTO.setApplies(dashboadCount(countApplies,label));

        dashboardDTO.setRevenues(dashboadCountRevenues(countRevenues,label));
        dashboardDTO.setTotalCareer(userRepository.countCareer());
        dashboardDTO.setTotalSubCareer(userRepository.countSubCareer());
        dashboardDTO.setTotalMemberShip(userRepository.countMemberShip());
        dashboardDTO.setTotalReport(userRepository.countReport());
        return dashboardDTO;
    }
    public List<Integer> dashboadCount(List<Object[]> objects,List<String> label){
        List<Integer> counts = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0));
        List<CountDto> countDTOs=new ArrayList<>();
        if(objects!=null)
        {
        for(Object[] ob:objects)
        {
            CountDto countDto=new CountDto();
            countDto.setMonth(ob[0].toString());
            countDto.setCountNumber(Integer.parseInt(ob[1].toString()));
            countDTOs.add(countDto);
        }
        for(int i=0;i<7;i++)
        {
            for (CountDto countDto:countDTOs){
                if(label.get(i).contains(countDto.getMonth())) {
                    counts.set(i, countDto.getCountNumber());
                    break;
                }
            }
        }
        }
        return counts;
    }
    public List<Double> dashboadCountRevenues(List<Object[]> objects,List<String> label){
        List<Double> counts = new ArrayList<>(Arrays.asList(0.0,0.0,0.0,0.0,0.0,0.0,0.0));
        List<RevenueDTO> revenueDTOS=new ArrayList<>();
        if(objects!=null) {
            for (Object[] ob : objects) {
                RevenueDTO revenueDTO = new RevenueDTO();
                revenueDTO.setMonth(ob[0].toString());
                revenueDTO.setCountNumber(Double.parseDouble(ob[1].toString()));
                revenueDTOS.add(revenueDTO);
            }
            for (int i = 0; i < 7; i++) {
                for (RevenueDTO revenueDTO : revenueDTOS) {
                    if (label.get(i).contains(revenueDTO.getMonth())) {
                        counts.set(i, revenueDTO.getCountNumber());
                        break;
                    }
                }
            }
        }
        return counts;
    }
}
