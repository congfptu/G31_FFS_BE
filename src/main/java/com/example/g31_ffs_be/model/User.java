package com.example.g31_ffs_be.model;

import com.example.g31_ffs_be.dto.ServiceDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class User {
    @Id
    @Column(name = "user_id", nullable = false, length = 45)
    private String id;
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private Account account;

    @Column(name = "account_balance",columnDefinition = "double default 0")
    private Double accountBalance;

    @Column(name = "phone", length = 45)
    private String phone;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "address")
    private String address;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch =FetchType.LAZY,optional = false)
    @JsonIgnore
    private Recruiter recruiter;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,fetch =FetchType.LAZY,optional = false)
    @JsonIgnore
    private Freelancer freelancer;

    @OneToMany(mappedBy = "from")
    @JsonIgnore
    private Set<Report> reports = new LinkedHashSet<>();

    @Column(name = "is_banned",columnDefinition = "bit default 1")
    private Boolean isBanned;

    @Column(name = "unread",columnDefinition = "int default 0")
    private Integer unRead;
    @OneToMany(mappedBy = "to")
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private Set<Feedback> feedbackTos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "from")
    @JsonIgnore
    private Set<Feedback> feedbackFroms = new LinkedHashSet<>();

    @Size(max = 255)
    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "reset_password_time")
    private Instant resetPasswordTime;

    @Column(name = "is_member_ship",columnDefinition = "bit default 0")
    private Boolean isMemberShip;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<RequestPayment> requestPayments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private Set<UserService> userServices = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Ban> bans = new LinkedHashSet<>();

    @OneToMany(mappedBy = "from")
    @JsonIgnore
    private Set<Notification> notificationFroms = new LinkedHashSet<>();

    @OneToMany(mappedBy = "to")
    @JsonIgnore
    private Set<Notification> notificationTos = new LinkedHashSet<>();

    public User(String id) {
        this.id = id;
    }


    @Transient
    private double star;
    @Transient
    private ServiceDto serviceDto;

    public ServiceDto getServiceDto() {
        ServiceDto serviceDto1=new ServiceDto();
        UserService currentService=new UserService();
        if(!isMemberShip||userServices.size()==0) return null;
        else{
            for(UserService us:userServices)
            {
                currentService=us;
                break;
            }
            for(UserService us:userServices)
            {
              if(us.getDateBuy().compareTo(currentService.getDateBuy())>0){
                  currentService=us;
              }
            }
            LocalDateTime time = currentService.getDateBuy();
            long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
            int dayRemain= (int) (currentService.getService().getDuration()-days);
            if(dayRemain<=0) return null;
            serviceDto1.setTimeRemain(dayRemain);
            serviceDto1.setId(currentService.getService().getId());
            serviceDto1.setServiceName(currentService.getService().getServiceName());
        }

        return serviceDto1;
    }

    public void setServiceDto(ServiceDto serviceDto) {
        this.serviceDto = serviceDto;
    }

    public double getStar() {
        double result = 0;
        Set<Feedback> feedbacks=feedbackTos;
        if(feedbacks.size()>0) {
            for (Feedback feedback : feedbacks)
                result += feedback.getStar();
            result = result / feedbacks.size();
        }
        NumberFormat formatter = new DecimalFormat("#0.0");
        String avgStar=formatter.format(result);

        return Double.parseDouble(avgStar);
    }

    public void setStar(double star) {
        this.star = star;
    }
}